package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.dao.PatientDao;
import net.thumbtack.school.hospital.dao.dao.TicketDao;
import net.thumbtack.school.hospital.dao.mybatis.daoimpl.DoctorDaoImpl;
import net.thumbtack.school.hospital.dao.mybatis.daoimpl.PatientDaoImpl;
import net.thumbtack.school.hospital.dao.mybatis.daoimpl.TicketDaoImpl;
import net.thumbtack.school.hospital.dto.internal.DoctorInfo;
import net.thumbtack.school.hospital.dto.request.AddPatientToAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.AddPatientToAppointmentDtoResponse;
import net.thumbtack.school.hospital.dto.response.GetAllTicketsDtoResponse;
import net.thumbtack.school.hospital.dto.response.GetTicketDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class TestPatientService {

    private PatientDao patientDao;
    private DoctorDao doctorDao;
    private TicketDao ticketDao;
    private PatientService patientService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        patientDao = mock(PatientDaoImpl.class);
        doctorDao = mock(DoctorDaoImpl.class);
        ticketDao = mock(TicketDaoImpl.class);
        patientService = new PatientService(patientDao, doctorDao, ticketDao);
    }

    @Test
    public void testRegisterPatient() throws HospitalException {
        RegisterPatientDtoRequest registerPatientDtoRequest = new RegisterPatientDtoRequest("name",
                "surname", "patronymic", "patientLogin", "password111",
                "111@mail.ru", "c.Omsk", "232323");

        Patient patient = modelMapper.map(registerPatientDtoRequest, Patient.class);
        patient.setId(2);

        when(patientDao.insert(any())).thenReturn(patient);

        ReturnPatientDtoResponse returnPatientDtoResponse = patientService.registerPatient(registerPatientDtoRequest);
        ReturnPatientDtoResponse result = modelMapper.map(patient, ReturnPatientDtoResponse.class);
        assertEquals(result, returnPatientDtoResponse);
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

        AddPatientToAppointmentDtoResponse expected = new AddPatientToAppointmentDtoResponse("D14202002041120",
                doctor2, "2020-02-04", "11:20");

        assertEquals(expected, result);
    }

    @Test
    public void testCancelAppointment() throws HospitalException {

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

        Ticket ticketForAppointment = new Ticket("ticket for appointment", patient);

        ticketForAppointment.setAppointment(doctor.getSchedule().get(0).getAppointmentList().get(0));
        ticketForAppointment.getAppointment().setDaySchedule(doctor.getSchedule().get(0));
        ticketForAppointment.getAppointment().getDaySchedule().setDoctor(doctor);

        when(patientDao.getById(anyInt())).thenReturn(patient);
        when(doctorDao.getById(anyInt())).thenReturn(doctor);
        when(ticketDao.getByNumber(anyString())).thenReturn(ticketForAppointment);
        doNothing().when(ticketDao).delete(any());

        patientService.cancelAppointment(patient.getId(), UserType.PATIENT, ticketForAppointment.getNumber());

        verify(ticketDao).delete(any());
    }

    @Test(expected = HospitalException.class)
    public void testCancelAppointmentFail() throws HospitalException {

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

        Ticket ticketForAppointment = new Ticket("ticket for appointment", patient);

        ticketForAppointment.setAppointment(doctor.getSchedule().get(0).getAppointmentList().get(0));
        ticketForAppointment.getAppointment().setDaySchedule(doctor.getSchedule().get(0));
        ticketForAppointment.getAppointment().getDaySchedule().setDoctor(doctor);

        when(patientDao.getById(anyInt())).thenReturn(patient);
        when(doctorDao.getById(anyInt())).thenReturn(doctor);
        when(ticketDao.getByNumber(anyString())).thenReturn(ticketForAppointment);
        doNothing().when(ticketDao).delete(any());

        patientService.cancelAppointment(1, UserType.PATIENT, ticketForAppointment.getNumber());

        verify(ticketDao).delete(any());
    }

    @Test
    public void testCancelCommission() {

        Patient patient = new Patient(2, UserType.PATIENT, "name", "surname",
                "patronymic", "Login", "oldPassword", "email@mail.ru",
                "address", "8-900-000-00-00");

        List<DaySchedule> schedule1 = new LinkedList<>(Collections.singletonList(
                new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:07"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:07"), LocalTime.parse("10:30"), AppointmentState.COMMISSION),
                        new Appointment(LocalTime.parse("10:30"), LocalTime.parse("11:00"), AppointmentState.COMMISSION),
                        new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:30"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("11:30"), LocalTime.parse("12:00"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("12:30"), LocalTime.parse("13:00"), AppointmentState.FREE))))));

        Doctor doctor1 = new Doctor(13, UserType.DOCTOR, "name1", "surname1",
                "patronymic1", "doctorLogin1", "doctorPass1", "хирург",
                "100", schedule1, new ArrayList<>());

        List<DaySchedule> schedule2 = new LinkedList<>(Collections.singletonList(
                new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:20"), AppointmentState.COMMISSION),
                        new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:40"), AppointmentState.COMMISSION))))));

        Doctor doctor2 = new Doctor(14, UserType.DOCTOR, "name2", "surname2",
                "patronymic2", "doctorLogin2", "doctorPass2", "хирург",
                "200", schedule2, new ArrayList<>());


        List<DaySchedule> scheduleForResult1 = new LinkedList<>(Collections.singletonList(
                new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:07"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:07"), LocalTime.parse("10:30"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:30"), LocalTime.parse("11:00"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:30"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("11:30"), LocalTime.parse("12:00"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("12:30"), LocalTime.parse("13:00"), AppointmentState.FREE))))));

        List<DaySchedule> scheduleForResult2 = new LinkedList<>(Collections.singletonList(
                new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:20"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:40"), AppointmentState.FREE))))));

        Doctor doctorForResult1 = new Doctor(13, UserType.DOCTOR, "name1", "surname1",
                "patronymic1", "doctorLogin1", "doctorPass1", "хирург",
                "100", scheduleForResult1, new ArrayList<>());

        Doctor doctorForResult2 = new Doctor(14, UserType.DOCTOR, "name2", "surname2",
                "patronymic2", "doctorLogin2", "doctorPass2", "хирург",
                "200", scheduleForResult2, new ArrayList<>());

        List<Doctor> list = Arrays.asList(doctor1, doctor2);

        Ticket ticket = new Ticket("ticket", patient);

        Commission commission = new Commission(LocalDate.of(2020, 1, 1),
                LocalTime.parse("10:15"), LocalTime.parse("10:35"),
                doctor1.getRoom(), list, ticket);

        ticket.setCommission(commission);

        when(patientDao.getById(anyInt())).thenReturn(patient);
        when(ticketDao.getByNumber(anyString())).thenReturn(commission.getTicket());

        List<Doctor> expectedList = Arrays.asList(doctorForResult1, doctorForResult2);

        patientService.cancelCommission("ticket");

        checkDoctorFields(list.get(0), expectedList.get(0));
        checkDoctorFields(list.get(1), expectedList.get(1));
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

        Commission commission = new Commission(LocalDate.of(2020, 1, 1),
                LocalTime.parse("10:15"), LocalTime.parse("10:35"),
                doctor1.getRoom(), Arrays.asList(doctor1, doctor2), ticketForCommission);

        ticketForCommission.setCommission(commission);

        List<Ticket> ticketList = Arrays.asList(ticketForAppointment, ticketForCommission);

        when(patientDao.getById(anyInt())).thenReturn(patient);
        when(patientDao.getAllTickets(any())).thenReturn(ticketList);

        GetAllTicketsDtoResponse result = patientService.getAllTickets(patient.getId());

        GetTicketDtoResponse response1 = new GetTicketDtoResponse(ticketForAppointment.getNumber(),
                doctor2.getRoom(), doctor2.getSchedule().get(1).getDate().toString(),
                ticketForAppointment.getAppointment().getTimeStart().toString(), doctor2.getId(),
                doctor2.getFirstName(), doctor2.getLastName(), doctor2.getPatronymic(), doctor2.getSpeciality());

        GetTicketDtoResponse response2 = new GetTicketDtoResponse(ticketForCommission.getNumber(),
                doctor1.getRoom(), commission.getDate().toString(), commission.getTimeStart().toString(),
                Arrays.asList(
                        new DoctorInfo(doctor1.getId(), doctor1.getFirstName(), doctor1.getLastName(),
                                doctor1.getPatronymic(), doctor1.getSpeciality()),
                        new DoctorInfo(doctor2.getId(), doctor2.getFirstName(), doctor2.getLastName(),
                                doctor2.getPatronymic(), doctor2.getSpeciality())));

        List<GetTicketDtoResponse> expectedList = Arrays.asList(response1, response2);

        assertEquals(expectedList, result.getResponseList());
    }

    private void checkDoctorFields(Doctor doctor1, Doctor doctor2) {
        assertEquals(doctor1.getId(), doctor2.getId());
        assertEquals(doctor1.getUserType(), doctor2.getUserType());
        assertEquals(doctor1.getFirstName(), doctor2.getFirstName());
        assertEquals(doctor1.getLastName(), doctor2.getLastName());
        assertEquals(doctor1.getPatronymic(), doctor2.getPatronymic());
        assertEquals(doctor1.getLogin(), doctor2.getLogin());
        assertEquals(doctor1.getPassword(), doctor2.getPassword());
        assertEquals(doctor1.getSpeciality(), doctor2.getSpeciality());
        assertEquals(doctor1.getRoom(), doctor2.getRoom());
        for (int i = 0; i < doctor1.getSchedule().size(); i++) {
            checkDayScheduleFields(doctor1.getSchedule().get(i), doctor2.getSchedule().get(i));
        }
        for (int i = 0; i < doctor1.getCommissionList().size(); i++) {
            checkCommissionFields(doctor1.getCommissionList().get(i), doctor2.getCommissionList().get(i));
        }
    }

    private void checkDayScheduleFields(DaySchedule daySchedule1, DaySchedule daySchedule2) {
        assertEquals(daySchedule1.getId(), daySchedule2.getId());
        assertEquals(daySchedule1.getDate(), daySchedule2.getDate());
        for (int i = 0; i < daySchedule1.getAppointmentList().size(); i++) {
            checkAppointmentFields(daySchedule1.getAppointmentList().get(i), daySchedule2.getAppointmentList().get(i));
        }
    }

    private void checkAppointmentFields(Appointment appointment1, Appointment appointment2) {
        assertEquals(appointment1.getId(), appointment2.getId());
        assertEquals(appointment1.getTimeStart(), appointment2.getTimeStart());
        assertEquals(appointment1.getTimeEnd(), appointment2.getTimeEnd());
        assertEquals(appointment1.getState(), appointment2.getState());
    }

    private void checkCommissionFields(Commission commission1, Commission commission2) {
        assertEquals(commission1.getId(), commission2.getId());
        assertEquals(commission1.getDate(), commission2.getDate());
        assertEquals(commission1.getTimeStart(), commission2.getTimeStart());
        assertEquals(commission1.getTimeEnd(), commission2.getTimeEnd());
        assertEquals(commission1.getRoom(), commission2.getRoom());
        assertEquals(commission1.getDoctorList().size(), commission2.getDoctorList().size());
        assertEquals(commission1.getTicket(), commission2.getTicket());
    }

}
