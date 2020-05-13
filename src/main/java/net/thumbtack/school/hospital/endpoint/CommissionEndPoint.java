package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.AddPatientToAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.request.AddPatientToCommissionDtoRequest;
import net.thumbtack.school.hospital.dto.response.AddPatientToAppointmentDtoResponse;
import net.thumbtack.school.hospital.dto.response.AddPatientToCommissionDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/commissions")
public class CommissionEndPoint {

    private PatientService patientService;
    private DoctorService doctorService;
    private UserService userService;

    @Autowired
    public CommissionEndPoint(DoctorService doctorService, PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AddPatientToCommissionDtoResponse addPatientToCommission(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            AddPatientToCommissionDtoRequest dtoRequest) throws HospitalException {

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        int id = userService.getIdBySession(JAVASESSIONID);

        if (userType.equals(UserType.DOCTOR)) {
            return doctorService.addPatientToCommission(dtoRequest, id);
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "only doctors can create commission");
    }

    @DeleteMapping(value = "/{ticketNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void cancelCommission(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @PathVariable("ticketNumber") String ticketNumber) throws HospitalException {

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType.equals(UserType.PATIENT)) {
            patientService.cancelCommission(ticketNumber);
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "only patients can cancel their commission");
    }
}
