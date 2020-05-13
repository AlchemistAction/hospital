package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.dao.AdminDao;
import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.model.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private AdminDao adminDao;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public AdminService(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    public ReturnAdminDtoResponse registerAdmin(RegisterAdminDtoRequest registerAdminDtoRequest) {

        Admin admin = modelMapper.map(registerAdminDtoRequest, Admin.class);
        admin.setUserType(UserType.ADMIN);
        admin = adminDao.insert(admin);
        return modelMapper.map(admin, ReturnAdminDtoResponse.class);
    }

    public ReturnAdminDtoResponse updateAdmin(UpdateAdminDtoRequest updateAdminDtoRequest, int id) throws HospitalException {

        Admin admin = adminDao.getById(id);
        if (!admin.getPassword().equals(updateAdminDtoRequest.getOldPassword())) {
            throw new HospitalException(HospitalErrorCode.WRONG_PASSWORD);
        }
        admin.setFirstName(updateAdminDtoRequest.getFirstName());
        admin.setLastName(updateAdminDtoRequest.getLastName());
        admin.setPatronymic(updateAdminDtoRequest.getPatronymic());
        admin.setPosition(updateAdminDtoRequest.getPosition());
        admin.setPassword(updateAdminDtoRequest.getNewPassword());

        admin = adminDao.update(admin);
        return modelMapper.map(admin, ReturnAdminDtoResponse.class);
    }
}
