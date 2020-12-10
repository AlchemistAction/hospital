package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.dao.AdminDao;
import net.thumbtack.school.hospital.dao.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.dao.PatientDao;
import net.thumbtack.school.hospital.dao.dao.UserDao;
import net.thumbtack.school.hospital.dao.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.dao.mybatis.daoimpl.DoctorDaoImpl;
import net.thumbtack.school.hospital.dao.mybatis.daoimpl.PatientDaoImpl;
import net.thumbtack.school.hospital.dao.mybatis.daoimpl.UserDaoImpl;
import net.thumbtack.school.hospital.dto.internal.WeekSchedule;
import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnUserDtoResponse;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

        when(patientDao.getByLogin(anyString())).thenReturn(patient);
        when(userDao.getUserTypeByLogin(any())).thenReturn(UserType.PATIENT);

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("patientLogin", "password");

        ReturnUserDtoResponse dtoResponse = userService.login(loginDtoRequest, "uuid");

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

        when(doctorDao.getByLogin(anyString())).thenReturn(doctor);
        when(userDao.getUserTypeByLogin(any())).thenReturn(UserType.DOCTOR);

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("doctorLogin", "doctorPassword");

        ReturnUserDtoResponse dtoResponse = userService.login(loginDtoRequest, "uuid");

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

        when(adminDao.getByLogin(anyString())).thenReturn(admin);
        when(userDao.getUserTypeByLogin(any())).thenReturn(UserType.ADMIN);

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("adminLogin", "adminPassword");

        ReturnUserDtoResponse dtoResponse = userService.login(loginDtoRequest, "uuid");

        assertEquals(expected, dtoResponse);
    }

    @Test(expected = HospitalException.class)
    public void testLoginFail1() throws HospitalException {

        when(userDao.getUserTypeByLogin(any())).thenReturn(null);

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("adminLogin", "adminPassword");
        userService.login(loginDtoRequest, "uuid");
    }

    @Test(expected = HospitalException.class)
    public void testLoginFail2() throws HospitalException {

        RegisterAdminDtoRequest registerAdminDtoRequest = new RegisterAdminDtoRequest("name",
                "surname", "patronymic", "regularAdmin", "adminLogin",
                "adminPassword");

        Admin admin = modelMapper.map(registerAdminDtoRequest, Admin.class);
        admin.setId(13);
        admin.setUserType(UserType.ADMIN);

        when(adminDao.getByLogin(anyString())).thenReturn(admin);

        when(userDao.getUserTypeByLogin(any())).thenReturn(UserType.ADMIN);

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("adminLogin", "adminPasswordWrong");
            userService.login(loginDtoRequest, "uuid");
    }
}
