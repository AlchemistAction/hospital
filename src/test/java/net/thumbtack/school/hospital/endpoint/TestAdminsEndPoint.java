package net.thumbtack.school.hospital.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminDtoRequest;
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
import static org.mockito.ArgumentMatchers.*;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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
                "ромагов", "романович", "regularAdmin", "adminlog",
                "adminpass");

        Admin admin = modelMapper.map(registerAdminDtoRequest, Admin.class);
        admin.setId(13);

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.ADMIN);
        when(adminService.registerAdmin(any())).thenReturn(modelMapper.map(admin, ReturnAdminDtoResponse.class));

        MvcResult result = mvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(registerAdminDtoRequest)))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testRegisterAdminFail1() throws Exception {
        RegisterAdminDtoRequest registerAdminDtoRequest = new RegisterAdminDtoRequest(null,
                "романов", "романович", "regularAdmin", "adminLog",
                "adminPass");

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.ADMIN);

        MvcResult result = mvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(registerAdminDtoRequest)))
                .andReturn();
        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(),
                GlobalErrorHandler.MyError.class);

        ErrorModel errorModel = new ErrorModel("VALIDATION_ERROR", "firstName", "Incorrect First name: ''");
        assertEquals(errorModel, error.getAllErrors().get(0));
    }

    @Test
    public void testRegisterAdminFail2() throws Exception {
        RegisterAdminDtoRequest registerAdminDtoRequest = new RegisterAdminDtoRequest("роман",
                "романов", "романович", "regularAdmin", "adminLog",
                "adminPass");

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.DOCTOR);

        MvcResult result = mvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(registerAdminDtoRequest)))
                .andReturn();
        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(),
                GlobalErrorHandler.MyError.class);

        ErrorModel errorModel = new ErrorModel("WRONG_USER_TYPE", "no field",
                "Only admins are allowed to register other admins");
        assertEquals(errorModel, error.getAllErrors().get(0));
    }

    @Test
    public void testUpdateAdmin() throws Exception {
        RegisterAdminDtoRequest registerAdminDtoRequest = new RegisterAdminDtoRequest("рома",
                "ромагов", "романович", "regularAdmin", "adminlog",
                "adminpass");
        Admin admin = modelMapper.map(registerAdminDtoRequest, Admin.class);
        admin.setId(13);

        UpdateAdminDtoRequest updateAdminDtoRequest = new UpdateAdminDtoRequest("стас",
                "иванов", "купецович", "regularAdmin", "oldPassword",
                "newPassword");

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.ADMIN);
        when(adminService.updateAdmin(any(), anyInt())).thenReturn(modelMapper.map(admin, ReturnAdminDtoResponse.class));

        MvcResult result = mvc.perform(put("/api/admins")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(updateAdminDtoRequest)))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }
    @Test
    public void testUpdateAdminFail() throws Exception {
        RegisterAdminDtoRequest registerAdminDtoRequest = new RegisterAdminDtoRequest("рома",
                "ромагов", "романович", "regularAdmin", "adminlog",
                "adminpass");
        Admin admin = modelMapper.map(registerAdminDtoRequest, Admin.class);
        admin.setId(13);

        UpdateAdminDtoRequest updateAdminDtoRequest = new UpdateAdminDtoRequest("стас",
                "иванов", "купецович", "regularAdmin", "oldPassword",
                "newPassword");

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.DOCTOR);
        when(adminService.updateAdmin(any(), anyInt())).thenReturn(modelMapper.map(admin, ReturnAdminDtoResponse.class));

        MvcResult result = mvc.perform(put("/api/admins")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(updateAdminDtoRequest)))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(),
                GlobalErrorHandler.MyError.class);

        ErrorModel errorModel = new ErrorModel("WRONG_USER_TYPE", "no field",
                "Admin can update his own info only");
        assertEquals(errorModel, error.getAllErrors().get(0));
    }
}
