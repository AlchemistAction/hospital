package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnUserDtoResponse;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.model.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.mybatis.dao.AdminDao;
import net.thumbtack.school.hospital.mybatis.dao.DoctorDao;
import net.thumbtack.school.hospital.mybatis.dao.PatientDao;
import net.thumbtack.school.hospital.mybatis.dao.UserDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private DoctorDao doctorDao;
    private PatientDao patientDao;
    private AdminDao adminDao;
    private UserDao userDao;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public UserService(DoctorDao doctorDao, PatientDao patientDao, UserDao userDao, AdminDao adminDao) {
        this.doctorDao = doctorDao;
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.adminDao = adminDao;
    }

    public ReturnUserDtoResponse login(LoginDtoRequest loginDtoRequest) throws HospitalException {

        LoginVerificator loginVerificator = userDao.getByLogin(loginDtoRequest.getLogin());
        if (loginVerificator == null) {
            throw new HospitalException(HospitalErrorCode.WRONG_LOGIN);
        }
        if (!loginVerificator.getPassword().equals(loginDtoRequest.getPassword())) {
            throw new HospitalException(HospitalErrorCode.WRONG_PASSWORD);
        }
        User user;

        switch (loginVerificator.getUserType()) {
            case ADMIN:
                user = adminDao.getById(loginVerificator.getId());
                return modelMapper.map(user, ReturnAdminDtoResponse.class);
            case DOCTOR:
                user = doctorDao.getById(loginVerificator.getId());
                return modelMapper.map(user, ReturnDoctorDtoResponse.class);
            case PATIENT:
                user = patientDao.getById(loginVerificator.getId());
                return modelMapper.map(user, ReturnPatientDtoResponse.class);
        }

        return null;
    }

    public ReturnUserDtoResponse getInfo(int id, String userType) throws HospitalException {

        User user = null;

        switch (UserType.valueOf(userType)) {
            case ADMIN:
                user = adminDao.getById(id);
                return modelMapper.map(user, ReturnAdminDtoResponse.class);
            case DOCTOR:
                user = doctorDao.getById(id);
                return modelMapper.map(user, ReturnDoctorDtoResponse.class);
            case PATIENT:
                user = patientDao.getById(id);
                return modelMapper.map(user, ReturnPatientDtoResponse.class);
        }

        if (user == null) {
            throw new HospitalException(HospitalErrorCode.CAN_NOT_FIND_USER);
        }

        return null;
    }
}
