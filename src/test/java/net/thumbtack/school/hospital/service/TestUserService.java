package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.internal.WeekSchedule;
import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
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
import net.thumbtack.school.hospital.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.mybatis.daoimpl.DoctorDaoImpl;
import net.thumbtack.school.hospital.mybatis.daoimpl.PatientDaoImpl;
import net.thumbtack.school.hospital.mybatis.daoimpl.UserDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUserService {

    private DoctorDao doctorDao;
    private PatientDao patientDao;
    private AdminDao adminDao;
    private UserDao userDao;
    private UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        adminDao = mock(AdminDaoImpl.class);
        doctorDao = mock(DoctorDaoImpl.class);
        patientDao = mock(PatientDaoImpl.class);
        userDao = mock(UserDaoImpl.class);
        userService = new UserService(doctorDao, patientDao, userDao, adminDao);
    }

    @Test
    public void testLoginPatient() throws HospitalException {

        RegisterPatientDtoRequest registerPatientDtoRequest = new RegisterPatientDtoRequest("name",
                "surname", "patronymic", "patientLogin", "password",
                "111@mail.ru", "c.Omsk", "232323");

        Patient patient = modelMapper.map(registerPatientDtoRequest, Patient.class);
        patient.setId(2);
        patient.setUserType(UserType.PATIENT);

        ReturnUserDtoResponse expected = modelMapper.map(patient, ReturnPatientDtoResponse.class);

        when(patientDao.getById(anyInt())).thenReturn(patient);
        when(userDao.getByLogin(any())).thenReturn(new LoginVerificator(2, UserType.PATIENT, "password"));

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("patientLogin", "password");

        ReturnUserDtoResponse dtoResponse = userService.login(loginDtoRequest);

        assertEquals(expected, dtoResponse);
    }

    @Test
    public void testLoginDoctor() throws HospitalException {

        RegisterDoctorDtoRequest registerDoctorDtoRequest = new RegisterDoctorDtoRequest("name",
                "surname", "patronymic", "хирург", "100", "doctorLogin",
                "doctorPassword", "13-04-2020", "16-04-2020",
                new WeekSchedule("10:00", "11:00", new String[]{"Monday", "Friday"}), "00:30");

        Doctor doctor = modelMapper.map(registerDoctorDtoRequest, Doctor.class);
        doctor.setId(3);
        doctor.setUserType(UserType.DOCTOR);

        ReturnUserDtoResponse expected = modelMapper.map(doctor, ReturnDoctorDtoResponse.class);

        when(doctorDao.getById(anyInt())).thenReturn(doctor);
        when(userDao.getByLogin(any())).thenReturn(new LoginVerificator(3, UserType.DOCTOR, "doctorPassword"));

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("doctorLogin", "doctorPassword");

        ReturnUserDtoResponse dtoResponse = userService.login(loginDtoRequest);

        assertEquals(expected, dtoResponse);
    }

    @Test
    public void testLoginAdmin() throws HospitalException {

        RegisterAdminDtoRequest registerAdminDtoRequest = new RegisterAdminDtoRequest("name",
                "surname", "patronymic", "regularAdmin", "adminLogin",
                "adminPassword");

        Admin admin = modelMapper.map(registerAdminDtoRequest, Admin.class);
        admin.setId(13);
        admin.setUserType(UserType.ADMIN);

        ReturnUserDtoResponse expected = modelMapper.map(admin, ReturnAdminDtoResponse.class);

        when(adminDao.getById(anyInt())).thenReturn(admin);
        when(userDao.getByLogin(any())).thenReturn(new LoginVerificator(13, UserType.ADMIN,"adminPassword"));

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("adminLogin", "adminPassword");

        ReturnUserDtoResponse dtoResponse = userService.login(loginDtoRequest);

        assertEquals(expected, dtoResponse);
    }

    @Test
    public void testLoginFail1() {

        when(userDao.getByLogin(any())).thenReturn(null);

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("adminLogin", "adminPassword");
        try {
            userService.login(loginDtoRequest);
            fail();
        } catch (HospitalException ex) {
            assertEquals(HospitalErrorCode.WRONG_LOGIN, ex.getErrorCode());
        }
    }

    @Test
    public void testLoginFail2() {

        when(userDao.getByLogin(any())).thenReturn(new LoginVerificator(13, UserType.ADMIN, "WrongPassword"));

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("adminLogin", "adminPassword");
        try {
            userService.login(loginDtoRequest);
            fail();
        } catch (HospitalException ex) {
            assertEquals(HospitalErrorCode.WRONG_PASSWORD, ex.getErrorCode());
        }
    }
}
