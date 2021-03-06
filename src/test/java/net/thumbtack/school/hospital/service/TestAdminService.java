package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.dao.AdminDao;
import net.thumbtack.school.hospital.dao.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class TestAdminService {

    private AdminDao adminDao;
    private AdminService adminService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        adminDao = mock(AdminDaoImpl.class);
        adminService = new AdminService(adminDao);
    }

    @Test
    public void testRegisterAdmin() throws HospitalException {
        RegisterAdminDtoRequest registerAdminDtoRequest = new RegisterAdminDtoRequest("name",
                "surname", "patronymic", "regularAdmin", "adminLogin",
                "adminPassword");

        Admin admin = modelMapper.map(registerAdminDtoRequest, Admin.class);
        admin.setId(13);

        when(adminDao.insert(any())).thenReturn(admin);

        ReturnAdminDtoResponse returnAdminDtoResponse = adminService.registerAdmin(registerAdminDtoRequest);
        ReturnAdminDtoResponse result = modelMapper.map(admin, ReturnAdminDtoResponse.class);

        assertEquals(result, returnAdminDtoResponse);
    }

    @Test
    public void testUpdateAdmin() throws HospitalException {
        UpdateAdminDtoRequest updateAdminDtoRequest = new UpdateAdminDtoRequest("name",
                "newLastName", "patronymic", "regularAdmin", "oldPassword",
                "newPassword");

        Admin oldAdmin = new Admin(UserType.ADMIN, "name", "oldLastNAme",
                "patronymic", "adminLogin", "oldPassword", "regularAdmin");

        Admin newAdmin = new Admin(UserType.ADMIN, "name", "newLastName",
                "patronymic", "adminLogin", "newPassword", "regularAdmin");

        when(adminDao.getById(anyInt())).thenReturn(oldAdmin);
        when(adminDao.update(any())).thenReturn(newAdmin);

        adminService.updateAdmin(updateAdminDtoRequest, oldAdmin.getId());

        assertEquals("newPassword", newAdmin.getPassword());
        assertEquals("newLastName", newAdmin.getLastName());
    }

    @Test(expected = HospitalException.class)
    public void testUpdateAdminFail() throws HospitalException {
        UpdateAdminDtoRequest updateAdminDtoRequest = new UpdateAdminDtoRequest("name",
                "newLastName", "patronymic", "regularAdmin", "oldPasswordWrong",
                "newPassword");

        Admin oldAdmin = new Admin(UserType.ADMIN, "name", "oldLastNAme",
                "patronymic", "adminLogin", "oldPassword", "regularAdmin");

        when(adminDao.getById(anyInt())).thenReturn(oldAdmin);

        adminService.updateAdmin(updateAdminDtoRequest, oldAdmin.getId());
    }
}
