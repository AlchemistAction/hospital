package net.thumbtack.school.hospital.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.service.AdminService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AdminsEndPoint.class)
@ComponentScan("net.thumbtack.school.hospital")
public class TestAdminsEndPoint {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private AdminService adminService;
    @MockBean
    private UserService userService;

    @Test
    public void testRegisterAdmin() throws Exception {
        RegisterAdminDtoRequest registerAdminDtoRequest = new RegisterAdminDtoRequest("рома",
                "ромагов", "patronymic", "regularAdmin", "adminLogin",
                "adminPassword");

        Admin admin = modelMapper.map(registerAdminDtoRequest, Admin.class);
        admin.setId(13);

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.ADMIN);
        when(adminService.registerAdmin(any())).thenReturn(modelMapper.map(admin, ReturnAdminDtoResponse.class));

        MvcResult result = mvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(registerAdminDtoRequest)))
                .andReturn();

        assertEquals(result.getResponse().getStatus(), 200);
    }

    @Test
    public void testRegisterAdminFail() throws Exception {
        RegisterAdminDtoRequest registerAdminDtoRequest = new RegisterAdminDtoRequest(null,
                "романов", "patronymic", "regularAdmin", "adminLogin",
                "adminPassword");

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.ADMIN);

        MvcResult result = mvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(registerAdminDtoRequest)))
                .andReturn();
        assertEquals(result.getResponse().getStatus(), 400);
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(),
                GlobalErrorHandler.MyError.class);

        ErrorModel errorModel = new ErrorModel("VALIDATION_ERROR","firstName", "First name: ''");
        assertEquals(errorModel, error.getAllErrors().get(0));
    }


}
