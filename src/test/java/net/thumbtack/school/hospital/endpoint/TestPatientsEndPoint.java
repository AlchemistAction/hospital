package net.thumbtack.school.hospital.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.service.UserService;
import net.thumbtack.school.hospital.validator.ErrorModel;
import net.thumbtack.school.hospital.validator.GlobalErrorHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
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
import static org.mockito.ArgumentMatchers.*;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PatientsEndPoint.class)
@ComponentScan("net.thumbtack.school.hospital")
public class TestPatientsEndPoint {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private PatientService patientService;
    @MockBean
    private UserService userService;

    @Test
    public void testRegisterPatient() throws Exception {

        RegisterPatientDtoRequest registerPatientDtoRequest = new RegisterPatientDtoRequest("роман",
                "романов", "романов", "patientL", "passwor",
                "111@mail.ru", "c.Omsk", "89139992323");

        Patient patient = modelMapper.map(registerPatientDtoRequest, Patient.class);
        patient.setId(2);

        when(patientService.registerPatient(any())).thenReturn(modelMapper.map(patient, ReturnPatientDtoResponse.class));
        MvcResult result = mvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerPatientDtoRequest)))
                .andReturn();

        assertEquals(result.getResponse().getStatus(), 200);
    }

    @Test
    public void testRegisterPatientFail() throws Exception {

        RegisterPatientDtoRequest registerPatientDtoRequest = new RegisterPatientDtoRequest("name",
                "name", "name", "patiedddddddntL", "passwdddddddddddor",
                "11ru", " ", "33");

        Patient patient = modelMapper.map(registerPatientDtoRequest, Patient.class);
        patient.setId(2);

        when(patientService.registerPatient(any())).thenReturn(modelMapper.map(patient, ReturnPatientDtoResponse.class));
        MvcResult result = mvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerPatientDtoRequest)))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(),
                GlobalErrorHandler.MyError.class);

        assertEquals(8, error.getAllErrors().size());
    }

    @Test
    public void tesGetPatientFail() throws Exception {
        mvc.perform(get("/api/patients/123")).andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdatePatientFail() throws Exception {

        UpdatePatientDtoRequest updatePatientDtoRequest = new UpdatePatientDtoRequest("роман",
                "устюгов", "романович", "email@mail.ru",
                "newAddress", "89139992323", "passwor",
                "passworы");

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.DOCTOR);

        MvcResult result = mvc.perform(put("/api/patients")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(updatePatientDtoRequest)))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(),
                GlobalErrorHandler.MyError.class);

        ErrorModel errorModel = new ErrorModel("WRONG_USER_TYPE", "no field",
                "Patient can update his own info only");
        assertEquals(errorModel, error.getAllErrors().get(0));
    }
}
