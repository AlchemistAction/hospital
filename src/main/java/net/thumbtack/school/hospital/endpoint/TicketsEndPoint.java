package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.AddPatientToAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.response.AddPatientToAppointmentDtoResponse;
import net.thumbtack.school.hospital.dto.response.GetAllTicketsDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketsEndPoint {

    private PatientService patientService;
    private UserService userService;

    @Autowired
    public TicketsEndPoint(PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AddPatientToAppointmentDtoResponse addPatientToAppointment(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest) throws HospitalException {

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType.equals(UserType.PATIENT)) {
            int id = userService.getIdBySession(JAVASESSIONID);
            return patientService.addPatientToAppointment(addPatientToAppointmentDtoRequest, id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "You are not allowed to do this operation");
        }
    }

    @DeleteMapping(value = "/{ticketNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void cancelAppointment(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @PathVariable("ticketNumber") String ticketNumber) throws HospitalException {

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        int id = userService.getIdBySession(JAVASESSIONID);
        patientService.cancelAppointment(id, userType, ticketNumber);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetAllTicketsDtoResponse> getAllTickets(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID) throws HospitalException {

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType.equals(UserType.PATIENT)) {
            int id = userService.getIdBySession(JAVASESSIONID);
            return patientService.getAllTickets(id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "You are not allowed to do this operation");
        }
    }
}
