package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.internal.DoctorInfo;
import net.thumbtack.school.hospital.dto.request.AddPatientToAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.AddPatientToAppointmentDtoResponse;
import net.thumbtack.school.hospital.dto.response.GetAllTicketsDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.model.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.mybatis.dao.DoctorDao;
import net.thumbtack.school.hospital.mybatis.dao.PatientDao;
import net.thumbtack.school.hospital.mybatis.daoimpl.DoctorDaoImpl;
import net.thumbtack.school.hospital.mybatis.daoimpl.PatientDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestPatientService {

    private PatientDao patientDao;
    private DoctorDao doctorDao;
    private PatientService patientService;
    private ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        patientDao = mock(PatientDaoImpl.class);
        doctorDao = mock(DoctorDaoImpl.class);
        patientService = new PatientService(patientDao, doctorDao);
    }

    @Test
    public void testRegisterPatient() {
        RegisterPatientDtoRequest registerPatientDtoRequest = new RegisterPatientDtoRequest("name",
                "surname", "patronymic", "patientLogin", "password111",
                "111@mail.ru", "c.Omsk", "232323");

        Patient patient = modelMapper.map(registerPatientDtoRequest, Patient.class);
        patient.setId(2);

        when(patientDao.insert(any())).thenReturn(patient);

        ReturnPatientDtoResponse returnPatientDtoResponse = patientService.registerPatient(registerPatientDtoRequest);

        assertEquals(2, returnPatientDtoResponse.getId());
    }

    @Test
    public void testUpdatePatient() throws HospitalException {
        UpdatePatientDtoRequest updatePatientDtoRequest = new UpdatePatientDtoRequest("name",
                "surname", "patronymic", "email@mail.ru",
                "newAddress", "8-900-000-00-00", "oldPassword",
                "newPassword");

        Patient oldPatient = new Patient(UserType.PATIENT, "name", "surname",
                "patronymic", "Login", "oldPassword", "email@mail.ru",
                "address", "8-900-000-00-00");

        Patient newPatient = new Patient(UserType.PATIENT, "name", "surname",
                "patronymic", "Login", "newPassword", "email@mail.ru",
                "newAddress", "8-900-000-00-00");

        when(patientDao.getById(anyInt())).thenReturn(oldPatient);
        when(patientDao.update(any())).thenReturn(newPatient);

        patientService.updatePatient(updatePatientDtoRequest, oldPatient.getId());

        assertEquals("newPassword", newPatient.getPassword());
        assertEquals("newAddress", newPatient.getAddress());
    }

    @Test
    public void testAddPatientToAppointment1() throws HospitalException {
        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest = new AddPatientToAppointmentDtoRequest(
                13, "13-04-2020", "10:00");

        List<DaySchedule> schedule = new LinkedList<>(Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 4, 13),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:30"), AppointmentState.FREE))),
                new DaySchedule(LocalDate.of(2020, 4, 15),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:30"), AppointmentState.FREE)))));

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
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE))),
                new DaySchedule(LocalDate.of(2020, 2, 2), Arrays.asList(
                        new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))));

        Doctor doctor1 = new Doctor(13, UserType.DOCTOR, "name", "surname",
                "patronymic", "doctorLogin", "doctorPass", "хирург",
                "100", schedule1);

        List<DaySchedule> schedule2 = Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 1, 3), Arrays.asList(
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE))),
                new DaySchedule(LocalDate.of(2020, 2, 4), Arrays.asList(
                        new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))));

        Doctor doctor2 = new Doctor(14, UserType.DOCTOR, "name", "surname",
                "patronymic", "doctorLogin", "doctorPass", "хирург",
                "200", schedule2);

        List<DaySchedule> schedule3 = Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 1, 5), Arrays.asList(
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE))),
                new DaySchedule(LocalDate.of(2020, 2, 6), Arrays.asList(
                        new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))));

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
    public void testAddPatientToAppointmentFail() {
        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest = new AddPatientToAppointmentDtoRequest(
                13, "13-04-2020", "10:00");

        List<DaySchedule> schedule = new LinkedList<>(Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 4, 13),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:30"), AppointmentState.APPOINTMENT))),
                new DaySchedule(LocalDate.of(2020, 4, 15),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:30"), AppointmentState.APPOINTMENT)))));

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

    @Test
    public void testGetAllTickets() {

        List<DaySchedule> schedule1 = Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 1, 1), Arrays.asList(
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.COMMISSION))),
                new DaySchedule(LocalDate.of(2020, 2, 2), Arrays.asList(
                        new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))));

        Doctor doctor1 = new Doctor(13, UserType.DOCTOR, "name1", "surname1",
                "patronymic1", "doctorLogin1", "doctorPass1", "хирург",
                "100", schedule1);

        List<DaySchedule> schedule2 = Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 1, 1), Arrays.asList(
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.COMMISSION))),
                new DaySchedule(LocalDate.of(2020, 2, 4), Arrays.asList(
                        new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.APPOINTMENT))));

        Doctor doctor2 = new Doctor(14, UserType.DOCTOR, "name2", "surname2",
                "patronymic2", "doctorLogin2", "doctorPass2", "хирург",
                "200", schedule2);

        Patient patient = new Patient(2, UserType.PATIENT, "name", "surname",
                "patronymic", "Login", "oldPassword", "email@mail.ru",
                "address", "8-900-000-00-00");

        Ticket ticketForAppointment = new Ticket("ticket for appointment", patient);

        Appointment appointment1 = doctor2.getSchedule().get(1).getAppointmentList().get(1);
        appointment1.setDaySchedule(doctor2.getSchedule().get(1));
        appointment1.getDaySchedule().setDoctor(doctor2);

        ticketForAppointment.setAppointment(appointment1);


        Ticket ticketForCommission = new Ticket("ticket for commission", patient);

        Appointment appointment2 = doctor1.getSchedule().get(0).getAppointmentList().get(1);
        appointment2.setDaySchedule(doctor1.getSchedule().get(0));
        appointment2.getDaySchedule().setDoctor(doctor1);
        Appointment appointment3 = doctor2.getSchedule().get(0).getAppointmentList().get(1);
        appointment3.setDaySchedule(doctor2.getSchedule().get(0));
        appointment3.getDaySchedule().setDoctor(doctor2);

        List<Appointment> appointmentList = Arrays.asList(appointment2, appointment3);

        Commission commission = new Commission(appointmentList, doctor1.getRoom(), ticketForCommission);

        ticketForCommission.setCommission(commission);

        List<Ticket> ticketList = Arrays.asList(ticketForAppointment, ticketForCommission);

        when(patientDao.getById(anyInt())).thenReturn(patient);
        when(patientDao.getAllTickets(any())).thenReturn(ticketList);

        List<GetAllTicketsDtoResponse> result = patientService.getAllTickets(patient.getId());

        GetAllTicketsDtoResponse response1 = new GetAllTicketsDtoResponse(ticketForAppointment.getName(),
                doctor2.getRoom(), doctor2.getSchedule().get(1).getDate().toString(),
                ticketForAppointment.getAppointment().getTimeStart().toString(), doctor2.getId(),
                doctor2.getFirstName(), doctor2.getLastName(), doctor2.getPatronymic(), doctor2.getSpeciality());

        GetAllTicketsDtoResponse response2 = new GetAllTicketsDtoResponse(ticketForCommission.getName(),
                doctor1.getRoom(), doctor1.getSchedule().get(0).getDate().toString(),
                doctor1.getSchedule().get(0).getAppointmentList().get(1).getTimeStart().toString(),
                Arrays.asList(
                        new DoctorInfo(doctor1.getId(), doctor1.getFirstName(), doctor1.getLastName(),
                                doctor1.getPatronymic(), doctor1.getSpeciality()),
                        new DoctorInfo(doctor2.getId(), doctor2.getFirstName(), doctor2.getLastName(),
                                doctor2.getPatronymic(), doctor2.getSpeciality())));

        List<GetAllTicketsDtoResponse> expectedList = Arrays.asList(response1, response2);

        assertEquals(expectedList, result);
    }

}
