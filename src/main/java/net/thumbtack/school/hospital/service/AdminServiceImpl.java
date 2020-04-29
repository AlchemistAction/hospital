package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegisterAdminDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.mybatis.dao.AdminDao;

public class AdminServiceImpl implements AdminService {

    private AdminDao adminDao;

    public AdminServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public RegisterAdminDtoResponse registerAdmin(RegisterAdminDtoRequest registerAdminDtoRequest) {
        Admin admin = new Admin(UserType.ADMIN, registerAdminDtoRequest.getFirstName(),
                registerAdminDtoRequest.getLastName(), registerAdminDtoRequest.getPatronymic(),
                registerAdminDtoRequest.getLogin(), registerAdminDtoRequest.getPassword(),
                registerAdminDtoRequest.getPosition());

        admin = adminDao.insert(admin);

        return new RegisterAdminDtoResponse(admin.getId(), admin.getFirstName(), admin.getLastName(),
                admin.getPatronymic(), admin.getPosition());
    }


}
