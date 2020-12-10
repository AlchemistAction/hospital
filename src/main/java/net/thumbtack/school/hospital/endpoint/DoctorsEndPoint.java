package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dao.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.model.EmptyJsonResponse;
import net.thumbtack.school.hospital.dto.request.DeleteDoctorScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.response.GetAllDoctorsDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.validator.ErrorModel;
import net.thumbtack.school.hospital.validator.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/doctors")
public class DoctorsEndPoint {

    private final DoctorService doctorService;
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Autowired
    public DoctorsEndPoint(DoctorService doctorService, UserService userService) {
        this.doctorService = doctorService;
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnDoctorDtoResponse registerDoctor(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @Valid @RequestBody RegisterDoctorDtoRequest registerDoctorDtoRequest) throws HospitalException {
        LOGGER.info("DoctorsEndPoint registerDoctor {} ", registerDoctorDtoRequest);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType != null && userType.equals(UserType.ADMIN)) {
            return doctorService.registerDoctor(registerDoctorDtoRequest);
        } else {
            LOGGER.debug("DoctorsEndPoint cant registerDoctor. wrong userType {} ", userType);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                    "Only admins are allowed to register other doctors"));
        }
    }

    @GetMapping(value = {"/{doctorId}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnDoctorDtoResponse getDoctor(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @PathVariable("doctorId") int doctorId,
            @RequestParam(value = "schedule", required = false, defaultValue = "no") String schedule,
            @RequestParam(value = "startDate", required = false, defaultValue = "no") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "no") String endDate) throws HospitalException {
        LOGGER.info("DoctorsEndPoint getDoctor {}, {}, {}, {}",  doctorId, schedule, startDate, endDate);
        if (JAVASESSIONID.equals("-1")) {
            LOGGER.debug("DoctorsEndPoint cant getDoctor. wrong JAVASESSIONID {}", JAVASESSIONID);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.AUTHORISATION_ERROR, "no field",
                    "You have to login First"));
        }
        Integer id = userService.getIdBySession(JAVASESSIONID);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        return doctorService.getDoctor(doctorId, schedule, startDate, endDate, id, userType);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public GetAllDoctorsDtoResponse getAllDoctors(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @RequestParam(value = "schedule", required = false, defaultValue = "no") String schedule,
            @RequestParam(value = "speciality", required = false, defaultValue = "no") String speciality,
            @RequestParam(value = "startDate", required = false, defaultValue = "no") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "no") String endDate) throws HospitalException {
        LOGGER.info("DoctorsEndPoint getAllDoctors {}, {}, {}, {}", speciality, schedule, startDate, endDate);
        if (JAVASESSIONID.equals("-1")) {
            LOGGER.debug("DoctorsEndPoint cant getDoctor. wrong JAVASESSIONID {}", JAVASESSIONID);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.AUTHORISATION_ERROR, "no field",
                    "You have to login First"));
        }
        Integer id = userService.getIdBySession(JAVASESSIONID);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);

        return doctorService.getAllDoctors(speciality, schedule, startDate, endDate, id, userType);
    }

    @PutMapping(value = "/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnDoctorDtoResponse updateSchedule(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @PathVariable("doctorId") int doctorId,
            @Valid @RequestBody UpdateScheduleDtoRequest dtoRequest) throws HospitalException {
        LOGGER.info("DoctorsEndPoint updateSchedule {}, {}", doctorId, dtoRequest);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType != null && userType.equals(UserType.ADMIN)) {
            return doctorService.updateSchedule(dtoRequest, doctorId);
        } else {
            LOGGER.debug("DoctorsEndPoint cant updateSchedule. wrong userType {} ", userType);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                    "Only admins are allowed to change Doctors Schedule"));
        }
    }

    @DeleteMapping(value = "/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteDoctorScheduleSinceDate(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @PathVariable("doctorId") int doctorId,
            @RequestBody DeleteDoctorScheduleDtoRequest dtoRequest) throws HospitalException {
        LOGGER.info("DoctorsEndPoint deleteDoctorScheduleSinceDate {}, {}", doctorId, dtoRequest);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType != null && userType.equals(UserType.ADMIN)) {
            doctorService.deleteDoctorScheduleSinceDate(dtoRequest, doctorId);
        } else {
            LOGGER.debug("DoctorsEndPoint cant deleteDoctorScheduleSinceDate. wrong userType {} ", userType);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                    "Only admins are allowed to delete Doctors Schedule"));
        }
        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }
}
