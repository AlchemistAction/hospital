package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.dao.AdminDao;
import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.validator.ErrorModel;
import net.thumbtack.school.hospital.validator.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminService {

    private final AdminDao adminDao;
    private final ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    public AdminService(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    public ReturnAdminDtoResponse registerAdmin(RegisterAdminDtoRequest registerAdminDtoRequest) throws HospitalException {
        LOGGER.info("Admin Service register new Admin {} ", registerAdminDtoRequest);
        Admin admin = modelMapper.map(registerAdminDtoRequest, Admin.class);
        admin.setUserType(UserType.ADMIN);
        try {
            admin = adminDao.insert(admin);
        } catch (RuntimeException ex) {
            LOGGER.debug("Admin Service cant register new Admin {}, {}", registerAdminDtoRequest.getLogin(), ex);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.DUPLICATE_USER, "login",
                    "User with login: " + "'" + registerAdminDtoRequest.getLogin() + "'" + " already exist"));
        }
        return modelMapper.map(admin, ReturnAdminDtoResponse.class);
    }

    public ReturnAdminDtoResponse updateAdmin(UpdateAdminDtoRequest updateAdminDtoRequest, int id) throws HospitalException {
        LOGGER.info("Admin Service update Admin {}, {}", updateAdminDtoRequest, id);
        Admin admin = adminDao.getById(id);
        if (!admin.getPassword().equals(updateAdminDtoRequest.getOldPassword())) {
            LOGGER.debug("Admin Service cant update Admin, wrong password {}, {}", updateAdminDtoRequest, id);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_PASSWORD,
                    "password", "Passwords don't match"));
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
