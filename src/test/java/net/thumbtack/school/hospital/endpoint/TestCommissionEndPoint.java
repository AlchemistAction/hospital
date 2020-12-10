package net.thumbtack.school.hospital.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.hospital.dto.request.AddPatientToCommissionDtoRequest;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.service.UserService;
import net.thumbtack.school.hospital.validator.ErrorModel;
import net.thumbtack.school.hospital.validator.GlobalErrorHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CommissionEndPoint.class)
@ComponentScan("net.thumbtack.school.hospital")
public class TestCommissionEndPoint {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private PatientService patientService;
    @MockBean
    private UserService userService;
    @MockBean
    private DoctorService doctorService;

    @Test
    public void testCreateCommission() throws Exception {
        AddPatientToCommissionDtoRequest dtoRequest = new AddPatientToCommissionDtoRequest(3,
                new Integer[]{13, 14}, "100", "01-01-2020", "10:15", "00:20");

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.DOCTOR);

        MvcResult result = mvc.perform(post("/api/commissions")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(dtoRequest)))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testCreateCommissionFail() throws Exception {
        AddPatientToCommissionDtoRequest dtoRequest = new AddPatientToCommissionDtoRequest(3,
                new Integer[]{13, 14}, "100", "01-01-2020", "10:15", "00:20");

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.PATIENT);

        MvcResult result = mvc.perform(post("/api/commissions")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(dtoRequest)))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(),
                GlobalErrorHandler.MyError.class);

        ErrorModel errorModel = new ErrorModel("WRONG_USER_TYPE", "no field",
                "Only doctors can create commission");
        assertEquals(errorModel, error.getAllErrors().get(0));
    }

    @Test
    public void testCancelCommission() throws Exception {

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.PATIENT);

        MvcResult result = mvc.perform(delete("/api/commissions/ticketNumber")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(Collections.EMPTY_LIST)))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testCancelCommissionFail() throws Exception {

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.DOCTOR);

        MvcResult result = mvc.perform(delete("/api/commissions/ticketNumber")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(Collections.EMPTY_LIST)))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(),
                GlobalErrorHandler.MyError.class);

        ErrorModel errorModel = new ErrorModel("WRONG_USER_TYPE", "no field",
                "Only patients can cancel their commission");
        assertEquals(errorModel, error.getAllErrors().get(0));
    }
}
