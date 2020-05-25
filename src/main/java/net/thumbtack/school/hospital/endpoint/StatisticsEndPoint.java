package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dao.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.dto.response.GetAllDoctorsStatistics;
import net.thumbtack.school.hospital.dto.response.UserStatisticsDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.service.UserService;
import net.thumbtack.school.hospital.validator.ErrorModel;
import net.thumbtack.school.hospital.validator.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsEndPoint {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Autowired
    public StatisticsEndPoint(DoctorService doctorService, PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.userService = userService;
    }

    @GetMapping(value = {"patients/{patientId}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserStatisticsDtoResponse getPatientStatistics(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @PathVariable("patientId") int patientId,
            @RequestParam(value = "startDate", required = false, defaultValue = "no") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "no") String endDate) throws HospitalException {
        LOGGER.info("StatisticsEndPoint getPatientStatistics {}, {}, {}", patientId, startDate, endDate);

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType != null && userType.equals(UserType.ADMIN)) {
            return patientService.getPatientStatistics(patientId, startDate, endDate);
        } else {
            LOGGER.debug("StatisticsEndPoint cant getPatientStatistics. wrong userType {} ", userType);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                    "Only admins are allowed to get Patient's statistic"));
        }
    }

    @GetMapping(value = {"doctors/{doctorId}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserStatisticsDtoResponse getDoctorStatistics(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @PathVariable("doctorId") int doctorId,
            @RequestParam(value = "startDate", required = false, defaultValue = "no") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "no") String endDate) throws HospitalException {
        LOGGER.info("StatisticsEndPoint getDoctorStatistics {}, {}, {}", doctorId, startDate, endDate);

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType != null && userType.equals(UserType.ADMIN)) {
            return doctorService.getDoctorStatistics(doctorId, startDate, endDate);
        } else {
            LOGGER.debug("StatisticsEndPoint cant getDoctorStatistics. wrong userType {} ", userType);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                    "Only admins are allowed to get Doctor's statistic"));
        }
    }

    @GetMapping(value = {"doctors"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public GetAllDoctorsStatistics getAllDoctorsStatistics(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @RequestParam(value = "speciality", required = false, defaultValue = "no") String speciality,
            @RequestParam(value = "startDate", required = false, defaultValue = "no") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "no") String endDate) throws HospitalException {
        LOGGER.info("StatisticsEndPoint getDoctorStatistics {}, {}, {}", speciality, startDate, endDate);

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType != null && userType.equals(UserType.ADMIN)) {
            return doctorService.getAllDoctorsStatistics(speciality, startDate, endDate);
        } else {
            LOGGER.debug("StatisticsEndPoint cant getDoctorStatistics. wrong userType {} ", userType);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                    "Only admins are allowed to get Doctor's statistic"));
        }
    }

}
