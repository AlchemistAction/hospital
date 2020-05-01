package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.request.AddPatientToAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.AddPatientToAppointmentDtoResponse;
import net.thumbtack.school.hospital.dto.response.RegisterPatientDtoResponse;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.model.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.mybatis.dao.DoctorDao;
import net.thumbtack.school.hospital.mybatis.dao.PatientDao;
import net.thumbtack.school.hospital.mybatis.dao.UserDao;
import net.thumbtack.school.hospital.mybatis.daoimpl.DoctorDaoImpl;
import net.thumbtack.school.hospital.mybatis.daoimpl.PatientDaoImpl;
import net.thumbtack.school.hospital.mybatis.daoimpl.UserDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestPatientService {

    private PatientDao patientDao;
    private DoctorDao doctorDao;
    private UserDao userDao;
    private PatientService patientService;
    private ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        patientDao = mock(PatientDaoImpl.class);
        userDao = mock(UserDaoImpl.class);
        doctorDao = mock(DoctorDaoImpl.class);
        patientService = new PatientService(patientDao, doctorDao, userDao);
    }

    @Test
    public void testRegisterPatient() {
        RegisterPatientDtoRequest registerPatientDtoRequest = new RegisterPatientDtoRequest("name",
                "surname", "patronymic", "patientLogin", "password111",
                "111@mail.ru", "c.Omsk", "232323");

        Patient patient = modelMapper.map(registerPatientDtoRequest, Patient.class);
        patient.setId(2);

        when(patientDao.insert(any())).thenReturn(patient);

        RegisterPatientDtoResponse registerPatientDtoResponse = patientService.registerPatient(registerPatientDtoRequest);

        assertEquals(2, registerPatientDtoResponse.getId());
    }

    @Test
    public void testUpdatePatient() {
        UpdatePatientDtoRequest updatePatientDtoRequest = new UpdatePatientDtoRequest("name",
                "surname", "patronymic", "email@mail.ru",
                "address", "8-900-000-00-00", "oldPassword",
                "newPassword");

        Patient patient = new Patient(UserType.PATIENT, "name", "surname",
                "patronymic", "Login", "oldPassword", "email@mail.ru",
                "address", "8-900-000-00-00");

        when(patientDao.getById(anyInt())).thenReturn(patient);

        patientService.updatePatient(updatePatientDtoRequest, patient.getId());

        assertEquals("newPassword", patient.getPassword());
    }

    @Test
    public void testAddPatientToAppointment1() throws HospitalException {
        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest = new AddPatientToAppointmentDtoRequest(
                13, "13-04-2020", "10:00");

        List<DaySchedule> schedule = new LinkedList<>(Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 4, 13),
                        Collections.singletonList(
                                new Appointment("10:00", "10:30", AppointmentState.FREE))),
                new DaySchedule(LocalDate.of(2020, 4, 15),
                        Collections.singletonList(
                                new Appointment("11:00", "11:30", AppointmentState.FREE)))));

        Doctor doctor = new Doctor(13, UserType.DOCTOR, "name", "surname",
                "patronymic", "doctorLogin", "doctorPass", "хирург",
                "100", schedule);

        Patient patient = new Patient(2, UserType.PATIENT, "name", "surname",
                "patronymic", "Login", "oldPassword", "email@mail.ru",
                "address", "8-900-000-00-00");

        when(patientDao.getById(anyInt())).thenReturn(patient);
        when(doctorDao.getById(anyInt())).thenReturn(doctor);

        AddPatientToAppointmentDtoResponse result = patientService.addPatientToAppointment(
                addPatientToAppointmentDtoRequest, patient.getId());

        assertEquals("D13202004131000", result.getTicket());
    }

    @Test
    public void testAddPatientToAppointment2() throws HospitalException {
        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest = new AddPatientToAppointmentDtoRequest(
                "хирург", "04-02-2020", "11:20");

        List<DaySchedule> schedule1 = Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 1, 1), Arrays.asList(
                        new Appointment("10:00", "10:19", AppointmentState.FREE),
                        new Appointment("10:20", "10:39", AppointmentState.FREE))),
                new DaySchedule(LocalDate.of(2020, 2, 2), Arrays.asList(
                        new Appointment("11:00", "11:19", AppointmentState.FREE),
                        new Appointment("11:20", "11:39", AppointmentState.FREE))));

        Doctor doctor1 = new Doctor(13, UserType.DOCTOR, "name", "surname",
                "patronymic", "doctorLogin", "doctorPass", "хирург",
                "100", schedule1);

        List<DaySchedule> schedule2 = Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 1, 3), Arrays.asList(
                        new Appointment("10:00", "10:19", AppointmentState.FREE),
                        new Appointment("10:20", "10:39", AppointmentState.FREE))),
                new DaySchedule(LocalDate.of(2020, 2, 4), Arrays.asList(
                        new Appointment("11:00", "11:19", AppointmentState.FREE),
                        new Appointment("11:20", "11:39", AppointmentState.FREE))));

        Doctor doctor2 = new Doctor(14, UserType.DOCTOR, "name", "surname",
                "patronymic", "doctorLogin", "doctorPass", "хирург",
                "200", schedule2);

        List<DaySchedule> schedule3 = Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 1, 5), Arrays.asList(
                        new Appointment("10:00", "10:19", AppointmentState.FREE),
                        new Appointment("10:20", "10:39", AppointmentState.FREE))),
                new DaySchedule(LocalDate.of(2020, 2, 6), Arrays.asList(
                        new Appointment("11:00", "11:19", AppointmentState.FREE),
                        new Appointment("11:20", "11:39", AppointmentState.FREE))));

        Doctor doctor3 = new Doctor(15, UserType.DOCTOR, "name", "surname",
                "patronymic", "doctorLogin", "doctorPass", "лор",
                "300", schedule3);

        Patient patient = new Patient(2, UserType.PATIENT, "name", "surname",
                "patronymic", "Login", "oldPassword", "email@mail.ru",
                "address", "8-900-000-00-00");

        List<Doctor> list = Arrays.asList(doctor1, doctor2, doctor3);

        when(patientDao.getById(anyInt())).thenReturn(patient);
        when(doctorDao.getAllBySpeciality(anyString())).thenReturn(list);

        AddPatientToAppointmentDtoResponse result = patientService.addPatientToAppointment(
                addPatientToAppointmentDtoRequest, patient.getId());

        assertEquals("D14202002041120", result.getTicket());
    }

    @Test
    public void testAddPatientToAppointmentFail() throws HospitalException {
        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest = new AddPatientToAppointmentDtoRequest(
                13, "13-04-2020", "10:00");

        List<DaySchedule> schedule = new LinkedList<>(Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 4, 13),
                        Collections.singletonList(
                                new Appointment("10:00", "10:30", AppointmentState.APPOINTMENT))),
                new DaySchedule(LocalDate.of(2020, 4, 15),
                        Collections.singletonList(
                                new Appointment("11:00", "11:30", AppointmentState.APPOINTMENT)))));

        Doctor doctor = new Doctor(13, UserType.DOCTOR, "name", "surname",
                "patronymic", "doctorLogin", "doctorPass", "хирург",
                "100", schedule);

        Patient patient = new Patient(2, UserType.PATIENT, "name", "surname",
                "patronymic", "Login", "oldPassword", "email@mail.ru",
                "address", "8-900-000-00-00");

        when(patientDao.getById(anyInt())).thenReturn(patient);
        when(doctorDao.getById(anyInt())).thenReturn(doctor);

        try {
            patientService.addPatientToAppointment(addPatientToAppointmentDtoRequest, patient.getId());
            fail();
        } catch (HospitalException ex) {
            assertEquals(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_APPOINTMENT, ex.getErrorCode());
        }
    }

}
