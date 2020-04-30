package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.mybatis.dao.AdminDao;
import net.thumbtack.school.hospital.mybatis.dao.UserDao;
import net.thumbtack.school.hospital.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.mybatis.daoimpl.UserDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class TestAdminService {

    private AdminDao adminDao;
    private UserDao userDao;
    private AdminService adminService;
    private ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        adminDao = mock(AdminDaoImpl.class);
        userDao = mock(UserDaoImpl.class);
        adminService = new AdminService(adminDao, userDao);
    }

    @Test
    public void testRegisterAdmin() {
        RegisterAdminDtoRequest registerAdminDtoRequest = new RegisterAdminDtoRequest("name",
                "surname", "patronymic", "regularAdmin", "adminLogin",
                "adminPassword");

        Admin admin = modelMapper.map(registerAdminDtoRequest, Admin.class);
        admin.setId(13);

        when(adminDao.insert(any())).thenReturn(admin);

        ReturnAdminDtoResponse returnAdminDtoResponse = adminService.registerAdmin(registerAdminDtoRequest);

        assertEquals(13, returnAdminDtoResponse.getId());
    }
}
