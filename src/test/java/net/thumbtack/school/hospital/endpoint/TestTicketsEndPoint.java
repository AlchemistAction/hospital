package net.thumbtack.school.hospital.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.hospital.dto.request.AddPatientToAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.UserType;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TicketsEndPoint.class)
@ComponentScan("net.thumbtack.school.hospital")
public class TestTicketsEndPoint {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private PatientService patientService;
    @MockBean
    private UserService userService;

    @Test
    public void testAddPatientToAppointment() throws Exception {
        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest = new AddPatientToAppointmentDtoRequest(
                13, "13-04-2020", "10:00");

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.ADMIN);

        MvcResult result = mvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(addPatientToAppointmentDtoRequest)))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(),
                GlobalErrorHandler.MyError.class);

        ErrorModel errorModel = new ErrorModel("WRONG_USER_TYPE", "no field",
                "Only Patients can make an appointment");
        assertEquals(errorModel, error.getAllErrors().get(0));
    }

    @Test
    public void testGetAllTicketsFail() throws Exception {
        mvc.perform(get("/api/tickets")).andExpect(status().isBadRequest());
    }

}
