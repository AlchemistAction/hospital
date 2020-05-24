package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dao.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.model.EmptyJsonResponse;
import net.thumbtack.school.hospital.dto.request.AddPatientToCommissionDtoRequest;
import net.thumbtack.school.hospital.dto.response.AddPatientToCommissionDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.validator.ErrorModel;
import net.thumbtack.school.hospital.validator.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commissions")
public class CommissionEndPoint {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Autowired
    public CommissionEndPoint(DoctorService doctorService, PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AddPatientToCommissionDtoResponse addPatientToCommission(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @RequestBody AddPatientToCommissionDtoRequest dtoRequest) throws HospitalException {
        LOGGER.info("CommissionEndPoint addPatientToCommission {} ", dtoRequest);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        int id = userService.getIdBySession(JAVASESSIONID);

        if (userType != null && userType.equals(UserType.DOCTOR)) {
            return doctorService.addPatientToCommission(dtoRequest, id);
        }
        LOGGER.debug("CommissionEndPoint cant addPatientToCommission. wrong userType {} ", userType);
        throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                "Only doctors can create commission"));
    }

    @DeleteMapping(value = "/{ticketNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity cancelCommission(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @PathVariable("ticketNumber") String ticketNumber) throws HospitalException {
        LOGGER.info("CommissionEndPoint cancelCommission {} ", ticketNumber);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType != null && userType.equals(UserType.PATIENT)) {
            patientService.cancelCommission(ticketNumber);
        } else {
            LOGGER.debug("CommissionEndPoint cant cancelCommission. wrong userType {} ", userType);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                    "Only patients can cancel their commission"));
        }
        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }
}
