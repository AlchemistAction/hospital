package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnUserDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.mybatis.dao.PatientDao;
import net.thumbtack.school.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/patients")
public class PatientsEndPoint {

    private PatientService patientService;
    private PatientDao patientDao;

    @Autowired
    public PatientsEndPoint(PatientService patientService, PatientDao patientDao) {
        this.patientService = patientService;
        this.patientDao = patientDao;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnPatientDtoResponse registerPatient(HttpServletResponse response,
                                                    @Valid @RequestBody RegisterPatientDtoRequest registerPatientDtoRequest) {

        ReturnPatientDtoResponse dtoResponse = patientService.registerPatient(registerPatientDtoRequest);

        Cookie cookie1 = new Cookie("userId", String.valueOf(dtoResponse.getId()));
        Cookie cookie2 = new Cookie("userType", String.valueOf(UserType.PATIENT));
        response.addCookie(cookie1);
        response.addCookie(cookie2);

        return dtoResponse;
    }

    @GetMapping(value = "/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnPatientDtoResponse getPatient(
            @CookieValue(value = "userId", defaultValue = "-1") int id,
            @CookieValue(value = "userType", defaultValue = "user") String userType,
            @PathVariable("patientId") int patientId) {

        if (userType.equals("ADMIN") || userType.equals("DOCTOR")) {
            return patientService.getPatient(patientId);
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Patient can not see info about other Patients");
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnPatientDtoResponse updatePatient(
            @CookieValue(value = "userId", defaultValue = "-1") int id,
            @CookieValue(value = "userType", defaultValue = "user") String userType,
            @Valid @RequestBody UpdatePatientDtoRequest updateAdminDtoRequest) throws HospitalException {

        return patientService.updatePatient(updateAdminDtoRequest, id);
    }
}
