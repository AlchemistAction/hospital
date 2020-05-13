package net.thumbtack.school.hospital.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.service.UserService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
    public void testRegisterDoctor() throws Exception {

        RegisterPatientDtoRequest registerPatientDtoRequest = new RegisterPatientDtoRequest("роман",
                "романов", "patronymic", "patientLogin", "password111",
                "111@mail.ru", "c.Omsk", "232323");

        Patient patient = modelMapper.map(registerPatientDtoRequest, Patient.class);
        patient.setId(2);

        when(patientService.registerPatient(any())).thenReturn(modelMapper.map(patient, ReturnPatientDtoResponse.class));
        MvcResult result = mvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerPatientDtoRequest)))
                .andReturn();

        assertEquals(result.getResponse().getStatus(), 200);
    }
}
