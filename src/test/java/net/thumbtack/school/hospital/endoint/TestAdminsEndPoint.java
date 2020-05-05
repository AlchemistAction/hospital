package net.thumbtack.school.hospital.endoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.thumbtack.school.hospital.dao.TestBase;
import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.endpoint.AdminsEndPoint;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AdminsEndPoint.class)
public class TestAdminsEndPoint extends TestBase {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private AdminDaoImpl adminDao;
    @MockBean
    private AdminService adminService;

    @Test
    public void testRegisterAdmin() throws Exception {
        RegisterAdminDtoRequest registerAdminDtoRequest = new RegisterAdminDtoRequest("name",
                "surname", "patronymic", "regularAdmin", "adminLogin",
                "adminPassword");
        Admin admin = modelMapper.map(registerAdminDtoRequest, Admin.class);
        admin.setId(13);
        when(adminDao.insert(any())).thenReturn(admin);
        when(adminService.registerAdmin(any())).thenReturn(modelMapper.map(admin, ReturnAdminDtoResponse.class));
        MvcResult result = mvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerAdminDtoRequest)))
                .andReturn();
        assertEquals(result.getResponse().getStatus(), 200);
        verify(adminDao).insert(admin);
    }

}
