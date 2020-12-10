package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dao.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.dto.request.AddPatientToAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.response.AddPatientToAppointmentDtoResponse;
import net.thumbtack.school.hospital.dto.response.GetAllTicketsDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.validator.ErrorModel;
import net.thumbtack.school.hospital.validator.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketsEndPoint {

    private final PatientService patientService;
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Autowired
    public TicketsEndPoint(PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AddPatientToAppointmentDtoResponse addPatientToAppointment(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @RequestBody AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest) throws HospitalException {
        LOGGER.info("TicketsEndPoint addPatientToAppointment {} ", addPatientToAppointmentDtoRequest);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType != null && userType.equals(UserType.PATIENT)) {
            int id = userService.getIdBySession(JAVASESSIONID);
            return patientService.addPatientToAppointment(addPatientToAppointmentDtoRequest, id);
        } else {
            LOGGER.debug("TicketsEndPoint cant addPatientToAppointment. wrong userType {} ", userType);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                    "Only Patients can make an appointment"));
        }
    }

    @DeleteMapping(value = "/{ticketNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void cancelAppointment(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @PathVariable("ticketNumber") String ticketNumber) throws HospitalException {
        LOGGER.info("TicketsEndPoint cancelAppointment {} ", ticketNumber);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        int id = userService.getIdBySession(JAVASESSIONID);
        patientService.cancelAppointment(id, userType, ticketNumber);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public GetAllTicketsDtoResponse getAllTickets(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID) throws HospitalException {
        LOGGER.info("TicketsEndPoint getAllTickets {} ", JAVASESSIONID);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType != null && userType.equals(UserType.PATIENT)) {
            int id = userService.getIdBySession(JAVASESSIONID);
            return patientService.getAllTickets(id);
        } else {
            LOGGER.debug("TicketsEndPoint cant getAllTickets. wrong userType {} ", userType);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                    "Only Patient see his tickets"));
        }
    }
}
