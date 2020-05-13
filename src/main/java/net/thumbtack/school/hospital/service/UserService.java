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
import net.thumbtack.school.hospital.model.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.model.exception.HospitalException;
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

    public ReturnUserDtoResponse login(LoginDtoRequest loginDtoRequest, String uuid) throws HospitalException {

        UserType userType = userDao.getUserTypeByLogin(loginDtoRequest.getLogin());
        if (userType == null) {
            throw new HospitalException(HospitalErrorCode.WRONG_LOGIN);
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
                throw new HospitalException(HospitalErrorCode.CAN_NOT_FIND_USER);
        }
    }

    private void checkPassword(String password, String passwordFromDb) throws HospitalException {
        if (!password.equals(passwordFromDb)) {
            throw new HospitalException(HospitalErrorCode.WRONG_PASSWORD);
        }
    }

    public void setSession(int id, String uuid) {
        userDao.setSession(id, uuid);
    }

    public ReturnUserDtoResponse getInfo(int id, UserType userType) throws HospitalException {
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
                throw new HospitalException(HospitalErrorCode.CAN_NOT_FIND_USER);
        }
    }

    public void logout(String uuid) {
        userDao.endSession(uuid);
    }

    public UserType getUserTypeBySession(String uuid) {
       return userDao.getUserTypeBySession(uuid);
    }

    public Integer getIdBySession(String uuid) {
        return userDao.getIdBySession(uuid);
    }
}
