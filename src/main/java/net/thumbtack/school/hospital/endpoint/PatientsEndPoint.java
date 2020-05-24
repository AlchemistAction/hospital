package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dao.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.validator.ErrorModel;
import net.thumbtack.school.hospital.validator.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
public class PatientsEndPoint {

    private final PatientService patientService;
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Autowired
    public PatientsEndPoint(PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnPatientDtoResponse registerPatient(
            HttpServletResponse response,
            @Valid @RequestBody RegisterPatientDtoRequest registerPatientDtoRequest) throws HospitalException {
        LOGGER.info("PatientsEndPoint registerPatient {} ", registerPatientDtoRequest);
        String uuid = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("JAVASESSIONID", uuid);
        response.addCookie(cookie);

        ReturnPatientDtoResponse dtoResponse = patientService.registerPatient(registerPatientDtoRequest);
        userService.setSession(dtoResponse.getId(), uuid);
        return dtoResponse;
    }

    @GetMapping(value = "/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnPatientDtoResponse getPatient(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @PathVariable("patientId") int patientId) throws HospitalException {
        LOGGER.info("PatientsEndPoint getPatient {} ", patientId);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);

        if (userType != null && (userType.equals(UserType.ADMIN) || userType.equals(UserType.DOCTOR))) {
            return patientService.getPatient(patientId);
        }
        LOGGER.debug("PatientsEndPoint cant getPatient. wrong userType {} ", userType);
        throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                "Only doctors and admins can see other Patients info"));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnPatientDtoResponse updatePatient(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @Valid @RequestBody UpdatePatientDtoRequest updateAdminDtoRequest) throws HospitalException {
        LOGGER.info("PatientsEndPoint updatePatient {} ", updateAdminDtoRequest);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType != null && userType.equals(UserType.PATIENT)) {
            int id = userService.getIdBySession(JAVASESSIONID);
            return patientService.updatePatient(updateAdminDtoRequest, id);
        } else {
            LOGGER.debug("PatientsEndPoint cant updatePatient. wrong userType {} ", userType);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                    "Patient can update his own info only"));
        }
    }
}
