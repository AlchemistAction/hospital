package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.mybatis.dao.AdminDao;
import net.thumbtack.school.hospital.mybatis.dao.UserDao;
import org.modelmapper.ModelMapper;

public class AdminService {

    private AdminDao adminDao;
    private UserDao userDao;
    private ModelMapper modelMapper = new ModelMapper();

    public AdminService(AdminDao adminDao, UserDao userDao) {
        this.adminDao = adminDao;
        this.userDao = userDao;
    }

    public ReturnAdminDtoResponse registerAdmin(RegisterAdminDtoRequest registerAdminDtoRequest) {

        Admin admin = modelMapper.map(registerAdminDtoRequest, Admin.class);

        admin = adminDao.insert(admin);

        return modelMapper.map(admin, ReturnAdminDtoResponse.class);
    }

    public ReturnAdminDtoResponse updateAdmin(UpdateAdminDtoRequest updateAdminDtoRequest, int id) {

        Admin admin = adminDao.getById(id);

        admin.setPassword(updateAdminDtoRequest.getNewPassword());

        userDao.update(admin);

        return modelMapper.map(admin, ReturnAdminDtoResponse.class);
    }
}
