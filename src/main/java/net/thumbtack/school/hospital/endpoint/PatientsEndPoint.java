package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.service.UserService;
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

    private PatientService patientService;
    private UserService userService;

    @Autowired
    public PatientsEndPoint(PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnPatientDtoResponse registerPatient(
            HttpServletResponse response,
            @Valid @RequestBody RegisterPatientDtoRequest registerPatientDtoRequest) {

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
            @PathVariable("patientId") int patientId) {

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);

        if (userType.equals(UserType.ADMIN) || userType.equals(UserType.DOCTOR)) {
            return patientService.getPatient(patientId);
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Patient can not see info about other Patients");
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnPatientDtoResponse updatePatient(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @Valid @RequestBody UpdatePatientDtoRequest updateAdminDtoRequest) throws HospitalException {

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType.equals(UserType.PATIENT)) {
            int id = userService.getIdBySession(JAVASESSIONID);
            return patientService.updatePatient(updateAdminDtoRequest, id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "You are not allowed to do this operation");
        }
    }
}
