package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.DeleteDoctorScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.service.DoctorService;
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

    @Autowired
    public DoctorsEndPoint(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnDoctorDtoResponse registerDoctor(
            @CookieValue(value = "userId", defaultValue = "-1") int id,
            @CookieValue(value = "userType", defaultValue = "user") String userType,
            @Valid @RequestBody RegisterDoctorDtoRequest registerDoctorDtoRequest) {

        if (userType.equals(String.valueOf(UserType.ADMIN))) {
            return doctorService.registerDoctor(registerDoctorDtoRequest);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Only admins are allowed to register other doctors");
        }
    }

    @GetMapping(value = "/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnDoctorDtoResponse getDoctor(
            @CookieValue(value = "userId", defaultValue = "-1") int id,
            @CookieValue(value = "userType", defaultValue = "user") String userType,
            @PathVariable("doctorId") int doctorId,
            @RequestParam(value = "schedule ", required = false, defaultValue = "no") String schedule,
            @RequestParam(value = "startDate ", required = false, defaultValue = "noStartDate") String startDate,
            @RequestParam(value = "endDate ", required = false, defaultValue = "endDate") String endDate) {

        return doctorService.getDoctor(doctorId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReturnDoctorDtoResponse> getAllDoctors(
            @CookieValue(value = "userId", defaultValue = "-1") int id,
            @CookieValue(value = "userType", defaultValue = "user") String userType,
            @RequestParam(value = "schedule", required = false, defaultValue = "no") String schedule,
            @RequestParam(value = "speciality", required = false, defaultValue = "speciality") String speciality,
            @RequestParam(value = "startDate", required = false, defaultValue = "noStartDate") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "endDate") String endDate) {

        return doctorService.getAllDoctors(speciality);
    }

    @PutMapping(value = "/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnDoctorDtoResponse updateSchedule(
            @CookieValue(value = "userId", defaultValue = "-1") int id,
            @CookieValue(value = "userType", defaultValue = "user") String userType,
            @PathVariable("doctorId") int doctorId,
            @Valid @RequestBody UpdateScheduleDtoRequest dtoRequest) throws HospitalException {

        return doctorService.updateSchedule(dtoRequest, doctorId);
    }

    @DeleteMapping(value = "/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteDoctorScheduleSinceDate(
            @CookieValue(value = "userId", defaultValue = "-1") int id,
            @CookieValue(value = "userType", defaultValue = "user") String userType,
            @PathVariable("doctorId") int doctorId,
            DeleteDoctorScheduleDtoRequest dtoRequest) throws HospitalException {

        if (userType.equals(String.valueOf(UserType.ADMIN))) {
            doctorService.deleteDoctorScheduleSinceDate(dtoRequest, doctorId);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Only admins are allowed to delete Doctors Schedule");
        }
    }
}
