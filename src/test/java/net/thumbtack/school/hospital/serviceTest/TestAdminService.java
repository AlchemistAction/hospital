package net.thumbtack.school.hospital.serviceTest;

import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegisterAdminDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.mybatis.dao.AdminDao;
import net.thumbtack.school.hospital.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.service.AdminService;
import net.thumbtack.school.hospital.service.AdminServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class TestAdminService {

    private AdminDao adminDao;
    private AdminService adminService;

    @Before
    public void setUp() {
        adminDao = mock(AdminDaoImpl.class);
        adminService = new AdminServiceImpl(adminDao);
    }

    @Test
    public void testRegisterAdmin() {
        RegisterAdminDtoRequest registerAdminDtoRequest = new RegisterAdminDtoRequest("name",
                "surname", "patronymic", "regularAdmin", "adminLogin",
                "adminPassword");

        Admin adminWithId = new Admin(13, UserType.ADMIN, registerAdminDtoRequest.getFirstName(),
                registerAdminDtoRequest.getLastName(), registerAdminDtoRequest.getPatronymic(),
                registerAdminDtoRequest.getLogin(), registerAdminDtoRequest.getPassword(),
                registerAdminDtoRequest.getPosition());

        when(adminDao.insert(any())).thenReturn(adminWithId);

        RegisterAdminDtoResponse registerAdminDtoResponse = adminService.registerAdmin(registerAdminDtoRequest);

        assertEquals(13, registerAdminDtoResponse.getId());
    }


}
