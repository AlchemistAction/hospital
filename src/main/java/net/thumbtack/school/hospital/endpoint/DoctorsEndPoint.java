package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.DeleteDoctorScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorsEndPoint {

    private DoctorService doctorService;
    private UserService userService;

    @Autowired
    public DoctorsEndPoint(DoctorService doctorService, UserService userService) {
        this.doctorService = doctorService;
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnDoctorDtoResponse registerDoctor(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @Valid @RequestBody RegisterDoctorDtoRequest registerDoctorDtoRequest) {

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType.equals(UserType.ADMIN)) {
            return doctorService.registerDoctor(registerDoctorDtoRequest);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Only admins are allowed to register other doctors");
        }
    }

    @GetMapping(value = "/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnDoctorDtoResponse getDoctor(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @PathVariable("doctorId") int doctorId,
            @RequestParam(value = "schedule ", required = false, defaultValue = "no") String schedule,
            @RequestParam(value = "startDate ", required = false, defaultValue = "noStartDate") String startDate,
            @RequestParam(value = "endDate ", required = false, defaultValue = "endDate") String endDate) {

        Integer id = userService.getIdBySession(JAVASESSIONID);
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "You have to login First");
        }
        return doctorService.getDoctor(doctorId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReturnDoctorDtoResponse> getAllDoctors(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @RequestParam(value = "schedule", required = false, defaultValue = "no") String schedule,
            @RequestParam(value = "speciality", required = false, defaultValue = "null") String speciality,
            @RequestParam(value = "startDate", required = false, defaultValue = "noStartDate") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "endDate") String endDate) {

        Integer id = userService.getIdBySession(JAVASESSIONID);
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "You have to login First");
        }
        return doctorService.getAllDoctors(speciality);
    }

    @PutMapping(value = "/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnDoctorDtoResponse updateSchedule(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @PathVariable("doctorId") int doctorId,
            @Valid @RequestBody UpdateScheduleDtoRequest dtoRequest) throws HospitalException {

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType.equals(UserType.ADMIN)) {
            return doctorService.updateSchedule(dtoRequest, doctorId);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Only admins are allowed to change Doctors Schedule");
        }
    }

    @DeleteMapping(value = "/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteDoctorScheduleSinceDate(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @PathVariable("doctorId") int doctorId,
            DeleteDoctorScheduleDtoRequest dtoRequest) throws HospitalException {

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType.equals(UserType.ADMIN)) {
            doctorService.deleteDoctorScheduleSinceDate(dtoRequest, doctorId);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Only admins are allowed to delete Doctors Schedule");
        }
    }
}
