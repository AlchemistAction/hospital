package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.dao.AdminDao;
import net.thumbtack.school.hospital.dao.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.dao.PatientDao;
import net.thumbtack.school.hospital.dao.dao.UserDao;
import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnUserDtoResponse;
import net.thumbtack.school.hospital.model.User;
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
public class UserService {

    private final DoctorDao doctorDao;
    private final PatientDao patientDao;
    private final AdminDao adminDao;
    private final UserDao userDao;
    private final ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(DoctorDao doctorDao, PatientDao patientDao, UserDao userDao, AdminDao adminDao) {
        this.doctorDao = doctorDao;
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.adminDao = adminDao;
    }

    public ReturnUserDtoResponse login(LoginDtoRequest loginDtoRequest, String uuid) throws HospitalException {
        LOGGER.info("User Service login {}, {}", loginDtoRequest, uuid);
        UserType userType = userDao.getUserTypeByLogin(loginDtoRequest.getLogin());
        if (userType == null) {
            LOGGER.debug("User Service cant login. wrong login {}, {}", loginDtoRequest, uuid);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_LOGIN,
                    "login", "There is no user with login: " + loginDtoRequest.getLogin()));
        }
        switch (userType) {
            case ADMIN:
                User admin = adminDao.getByLogin(loginDtoRequest.getLogin());
                checkPassword(loginDtoRequest.getPassword(), admin.getPassword());
                setSession(admin.getId(), uuid);

                return modelMapper.map(admin, ReturnAdminDtoResponse.class);
            case DOCTOR:
                User doctor = doctorDao.getByLogin(loginDtoRequest.getLogin());
                checkPassword(loginDtoRequest.getPassword(), doctor.getPassword());
                setSession(doctor.getId(), uuid);

                return modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
            case PATIENT:
                User patient = patientDao.getByLogin(loginDtoRequest.getLogin());
                checkPassword(loginDtoRequest.getPassword(), patient.getPassword());
                setSession(patient.getId(), uuid);

                return modelMapper.map(patient, ReturnPatientDtoResponse.class);
            default:
                LOGGER.debug("User Service cant login {}, {}", loginDtoRequest, uuid);
                throw new HospitalException(new ErrorModel(HospitalErrorCode.CAN_NOT_FIND_USER,
                        "no field", "Something went wrong. Try Again"));
        }
    }

    private void checkPassword(String password, String passwordFromDb) throws HospitalException {
        if (!password.equals(passwordFromDb)) {
            LOGGER.debug("User Service cant login. wrong password {}", password);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_PASSWORD,
                    "password", "Passwords don't match"));
        }
    }

    public void setSession(int id, String uuid) {
        LOGGER.info("User Service set session {}, {}", id, uuid);
        userDao.setSession(id, uuid);
    }

    public ReturnUserDtoResponse getInfo(int id, UserType userType) throws HospitalException {
        LOGGER.info("User Service get info {}, {}", id, userType);
        switch (userType) {
            case ADMIN:
                User admin = adminDao.getById(id);
                return modelMapper.map(admin, ReturnAdminDtoResponse.class);
            case DOCTOR:
                User doctor = doctorDao.getById(id);
                return modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
            case PATIENT:
                User patient = patientDao.getById(id);
                return modelMapper.map(patient, ReturnPatientDtoResponse.class);
            default:
                LOGGER.debug("User Service cant get info. wrong ID {}, {}", id, userType);
                throw new HospitalException(new ErrorModel(HospitalErrorCode.CAN_NOT_FIND_USER,
                        "id", "There is no user with ID: " + id));
        }
    }

    public void logout(String uuid) {
        LOGGER.info("User Service logout {}", uuid);
        userDao.endSession(uuid);
    }

    public UserType getUserTypeBySession(String uuid) {
        LOGGER.info("User Service getUserTypeBySession {}", uuid);
        return userDao.getUserTypeBySession(uuid);
    }

    public Integer getIdBySession(String uuid) {
        LOGGER.info("User Service getIdBySession {}", uuid);
        return userDao.getIdBySession(uuid);
    }
}
