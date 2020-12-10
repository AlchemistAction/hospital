package net.thumbtack.school.hospital.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.service.UserService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SessionsEndPoint.class)
@ComponentScan("net.thumbtack.school.hospital")
public class TestSessionsEndPoint {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserService userService;

    @Test
    public void testLogin() throws Exception {
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("patientLogin", "password");

        MvcResult result = mvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginDtoRequest)))
                .andReturn();

        assertEquals(result.getResponse().getStatus(), 200);
    }

    @Test
    public void testLogout() throws Exception {
      mvc.perform(delete("/api/sessions")).andExpect(status().isOk());
    }

}
