package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.TestBase;
import net.thumbtack.school.hospital.dto.request.AddPatientToCommissionDtoRequest;
import net.thumbtack.school.hospital.dto.request.ChangeScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.internal.DayScheduleForDto;
import net.thumbtack.school.hospital.dto.internal.WeekSchedule;
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
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestDoctorService {

    private DoctorDao doctorDao;
    private PatientDao patientDao;
    private DoctorService doctorService;
    private ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        doctorDao = mock(DoctorDaoImpl.class);
        patientDao = mock(PatientDaoImpl.class);
        doctorService = new DoctorService(doctorDao, patientDao);
    }

    @Test
    public void testRegisterDoctorWithWeekSchedule() {
        RegisterDoctorDtoRequest registerDoctorDtoRequest = new RegisterDoctorDtoRequest("name",
                "surname", "patronymic", "хирург", "100", "doctorLogin",
                "doctorPassword", "13-04-2020", "16-04-2020",
                new WeekSchedule("10:00", "11:00", new String[]{"Monday", "Friday"}), "00:30");

        List<DaySchedule> schedule = Collections.singletonList(
                new DaySchedule(LocalDate.of(2020, 4, 13),
                        Arrays.asList(
                                new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:30"), AppointmentState.FREE),
                                new Appointment(LocalTime.parse("10:30"), LocalTime.parse("11:00"), AppointmentState.FREE))));

        Doctor doctor = modelMapper.map(registerDoctorDtoRequest, Doctor.class);
        doctor.setSchedule(schedule);
        doctor.setId(3);

        when(doctorDao.insert(any())).thenReturn(doctor);

        ReturnDoctorDtoResponse returnDoctorDtoResponse = doctorService.registerDoctor(registerDoctorDtoRequest);

        Map<String, Map<String, Patient>> resultMap = new TreeMap<>();
        Map<String, Patient> timeMap = new TreeMap<>();
        timeMap.put("10:00", null);
        timeMap.put("10:30", null);

        resultMap.put("13-04-2020", timeMap);

        assertEquals(3, returnDoctorDtoResponse.getId());
        assertEquals(resultMap, returnDoctorDtoResponse.getSchedule());
    }

    @Test
    public void testRegisterDoctorWithWeekDaySchedule() {
        RegisterDoctorDtoRequest registerDoctorDtoRequest = new RegisterDoctorDtoRequest("name",
                "surname", "patronymic", "хирург", "100", "doctorLogin",
                "doctorPassword", "13-04-2020", "16-04-2020",
                new DayScheduleForDto[]{
                        new DayScheduleForDto("Monday", "10:00", "10:30"),
                        new DayScheduleForDto("Tuesday", "11:00", "11:30")},
                "00:30");

        List<DaySchedule> schedule = Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 4, 13),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:30"), AppointmentState.FREE))),
                new DaySchedule(LocalDate.of(2020, 4, 14),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:30"), AppointmentState.FREE))));


        Doctor doctor = modelMapper.map(registerDoctorDtoRequest, Doctor.class);
        doctor.setSchedule(schedule);
        doctor.setId(3);

        when(doctorDao.insert(any())).thenReturn(doctor);

        ReturnDoctorDtoResponse returnDoctorDtoResponse = doctorService.registerDoctor(registerDoctorDtoRequest);

        Map<String, Map<String, Patient>> resultMap = new TreeMap<>();

        Map<String, Patient> timeMap1 = new TreeMap<>();
        timeMap1.put("10:00", null);
        resultMap.put("13-04-2020", timeMap1);

        Map<String, Patient> timeMap2 = new TreeMap<>();
        timeMap2.put("11:00", null);
        resultMap.put("14-04-2020", timeMap2);

        assertEquals(resultMap, returnDoctorDtoResponse.getSchedule());
    }

    @Test
    public void testInsertNewSchedule1() throws HospitalException {

        ChangeScheduleDtoRequest changeScheduleDtoRequest = new ChangeScheduleDtoRequest(
                "15-04-2020",
                "18-04-2020",
                new DayScheduleForDto[]{
                        new DayScheduleForDto("Thursday", "10:00", "10:30"),
                        new DayScheduleForDto("Friday", "11:00", "11:30")},
                "00:30");

        List<DaySchedule> schedule = new LinkedList<>(Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 4, 13),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:30"), AppointmentState.FREE))),
                new DaySchedule(LocalDate.of(2020, 4, 14),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:30"), AppointmentState.FREE)))));

        List<DaySchedule> newSchedule = new LinkedList<>(Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 4, 16),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:30"), AppointmentState.FREE))),
                new DaySchedule(LocalDate.of(2020, 4, 17),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:30"), AppointmentState.FREE)))));

        Doctor doctor = new Doctor(UserType.DOCTOR, "name", "surname",
                "patronymic", "doctorLogin", "doctorPass", "хирург",
                "100", schedule);

        when(doctorDao.getById(anyInt())).thenReturn(doctor);
        when(doctorDao.insertSchedule(any(), any())).thenReturn(newSchedule);

        ReturnDoctorDtoResponse returnDoctorDtoResponse = doctorService.
                updateSchedule(changeScheduleDtoRequest, doctor.getId());

        Map<String, Map<String, Patient>> resultMap = new TreeMap<>();

        Map<String, Patient> timeMap1 = new TreeMap<>();
        timeMap1.put("10:00", null);
        resultMap.put("13-04-2020", timeMap1);

        Map<String, Patient> timeMap2 = new TreeMap<>();
        timeMap2.put("11:00", null);
        resultMap.put("14-04-2020", timeMap2);

        Map<String, Patient> timeMap3 = new TreeMap<>();
        timeMap3.put("10:00", null);
        resultMap.put("16-04-2020", timeMap3);

        Map<String, Patient> timeMap4 = new TreeMap<>();
        timeMap4.put("11:00", null);
        resultMap.put("17-04-2020", timeMap4);

        assertEquals(resultMap, returnDoctorDtoResponse.getSchedule());
    }

    @Test
    public void testInsertNewSchedule2() throws HospitalException {

        ChangeScheduleDtoRequest changeScheduleDtoRequest = new ChangeScheduleDtoRequest(
                "14-04-2020",
                "16-04-2020",
                new DayScheduleForDto[]{
                        new DayScheduleForDto("Wednesday", "22:00", "22:30"),
                        new DayScheduleForDto("Friday", "11:00", "11:30")},
                "00:30");

        List<DaySchedule> schedule = new LinkedList<>(Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 4, 13),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:30"), AppointmentState.FREE))),
                new DaySchedule(LocalDate.of(2020, 4, 15),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:30"), AppointmentState.FREE)))));

        DaySchedule newDaySchedule = new DaySchedule(LocalDate.of(2020, 4, 15),
                Collections.singletonList(
                        new Appointment(LocalTime.parse("22:00"), LocalTime.parse("22:30"), AppointmentState.FREE)));

        Doctor doctor = new Doctor(UserType.DOCTOR, "name", "surname",
                "patronymic", "doctorLogin", "doctorPass", "хирург",
                "100", schedule);

        when(doctorDao.getById(anyInt())).thenReturn(doctor);
        when(doctorDao.updateDaySchedule(anyInt(), any(), any())).thenReturn(newDaySchedule);

        ReturnDoctorDtoResponse returnDoctorDtoResponse = doctorService.
                updateSchedule(changeScheduleDtoRequest, doctor.getId());

        Map<String, Map<String, Patient>> resultMap = new TreeMap<>();

        Map<String, Patient> timeMap1 = new TreeMap<>();
        timeMap1.put("10:00", null);
        resultMap.put("13-04-2020", timeMap1);

        Map<String, Patient> timeMap2 = new TreeMap<>();
        timeMap2.put("22:00", null);
        resultMap.put("15-04-2020", timeMap2);

        assertEquals(resultMap, returnDoctorDtoResponse.getSchedule());
    }

    @Test
    public void testInsertNewScheduleFail() {

        ChangeScheduleDtoRequest changeScheduleDtoRequest = new ChangeScheduleDtoRequest(
                "14-04-2020",
                "16-04-2020",
                new DayScheduleForDto[]{
                        new DayScheduleForDto("Wednesday", "22:00", "22:30"),
                        new DayScheduleForDto("Friday", "11:00", "11:30")},
                "00:30");

        List<DaySchedule> schedule = new LinkedList<>(Arrays.asList(
                new DaySchedule(LocalDate.of(2020, 4, 13),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:30"), AppointmentState.APPOINTMENT))),
                new DaySchedule(LocalDate.of(2020, 4, 15),
                        Collections.singletonList(
                                new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:30"), AppointmentState.APPOINTMENT)))));

        Doctor doctor = new Doctor(UserType.DOCTOR, "name", "surname",
                "patronymic", "doctorLogin", "doctorPass", "хирург",
                "100", schedule);

        when(doctorDao.getById(anyInt())).thenReturn(doctor);

        try {
            doctorService.updateSchedule(changeScheduleDtoRequest, doctor.getId());
            fail();
        } catch (HospitalException ex) {
            assertEquals(HospitalErrorCode.CAN_NOT_UPDATE_SCHEDULE, ex.getErrorCode());
        }
    }

    @Test
    public void testAddPatientToCommission() throws HospitalException {

        Patient patient = new Patient(2, UserType.PATIENT, "name", "surname",
                "patronymic", "Login", "oldPassword", "email@mail.ru",
                "address", "8-900-000-00-00");

        List<DaySchedule> schedule1 = new LinkedList<>(Collections.singletonList(
                new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:07"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:07"), LocalTime.parse("10:30"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:30"), LocalTime.parse("11:00"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:30"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("11:30"), LocalTime.parse("12:00"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("12:30"), LocalTime.parse("13:00"), AppointmentState.FREE))))));

        Doctor doctor1 = new Doctor(13, UserType.DOCTOR, "name1", "surname1",
                "patronymic1", "doctorLogin1", "doctorPass1", "хирург",
                "100", schedule1);

        List<DaySchedule> schedule2 = new LinkedList<>(Collections.singletonList(
                new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:20"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:40"), AppointmentState.FREE))))));

        Doctor doctor2 = new Doctor(14, UserType.DOCTOR, "name2", "surname2",
                "patronymic2", "doctorLogin2", "doctorPass2", "хирург",
                "200", schedule2);


        Ticket ticket1 = new Ticket("ticket for app1", patient);
        ticket1.setAppointment(new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:10"), AppointmentState.COMMISSION,
                new DaySchedule(LocalDate.of(2020, 1, 1), new ArrayList<>())));
        Ticket ticket2 = new Ticket("ticket for app2", patient);
        ticket2.setAppointment(new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:20"), AppointmentState.COMMISSION,
                new DaySchedule(LocalDate.of(2020, 1, 1), new ArrayList<>())));
        Ticket ticket3 = new Ticket("ticket for app3", patient);
        ticket3.setAppointment(new Appointment(LocalTime.parse("10:45"), LocalTime.parse("11:00"), AppointmentState.COMMISSION,
                new DaySchedule(LocalDate.of(2020, 1, 1), new ArrayList<>())));

        List<Ticket> ticketList = Arrays.asList(ticket1, ticket2, ticket3);

        when(patientDao.getAllTickets(any())).thenReturn(ticketList);

        when(doctorDao.getById(anyInt())).thenReturn(doctor1).thenReturn(doctor2);
        when(patientDao.getById(anyInt())).thenReturn(patient);


        List<DaySchedule> scheduleForResult1 = new LinkedList<>(Collections.singletonList(
                new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:07"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:07"), LocalTime.parse("10:15"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:15"), LocalTime.parse("10:35"), AppointmentState.COMMISSION),
                        new Appointment(LocalTime.parse("10:35"), LocalTime.parse("11:00"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:30"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("11:30"), LocalTime.parse("12:00"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("12:30"), LocalTime.parse("13:00"), AppointmentState.FREE))))));

        List<DaySchedule> scheduleForResult2 = new LinkedList<>(Collections.singletonList(
                new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:15"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:15"), LocalTime.parse("10:35"), AppointmentState.COMMISSION),
                        new Appointment(LocalTime.parse("10:35"), LocalTime.parse("10:40"), AppointmentState.FREE))))));

        Doctor doctorForResult1 = new Doctor(13, UserType.DOCTOR, "name1", "surname1",
                "patronymic1", "doctorLogin1", "doctorPass1", "хирург",
                "100", scheduleForResult1);

        Doctor doctorForResult2 = new Doctor(14, UserType.DOCTOR, "name2", "surname2",
                "patronymic2", "doctorLogin2", "doctorPass2", "хирург",
                "200", scheduleForResult2);

        List<Doctor> expectedList = Arrays.asList(doctorForResult1, doctorForResult2);


        AddPatientToCommissionDtoRequest dtoRequest = new AddPatientToCommissionDtoRequest(patient.getId(),
                new Integer[]{13, 14}, "100", "01-01-2020", "10:15", "00:20");

        List<Doctor> doctorListFromService = doctorService.addPatientToCommission(dtoRequest, doctor1.getId());

        doctorListFromService.forEach(doctor -> doctor.getSchedule().
                forEach(daySchedule -> daySchedule.getAppointmentList().
                        sort(Comparator.comparing(Appointment::getTimeStart))));


        checkDoctorFields(expectedList.get(0), doctorListFromService.get(0));
        checkDoctorFields(expectedList.get(1), doctorListFromService.get(1));
    }

    @Test
    public void testAddPatientToCommissionFail1() {

        Patient patient = new Patient(2, UserType.PATIENT, "name", "surname",
                "patronymic", "Login", "oldPassword", "email@mail.ru",
                "address", "8-900-000-00-00");

        Ticket ticket1 = new Ticket("ticket for app1", patient);
        ticket1.setAppointment(new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:14"), AppointmentState.COMMISSION,
                new DaySchedule(LocalDate.of(2020, 1, 1), new ArrayList<>())));
        Ticket ticket2 = new Ticket("ticket for app2", patient);
        ticket2.setAppointment(new Appointment(LocalTime.parse("10:36"), LocalTime.parse("11:00"), AppointmentState.COMMISSION,
                new DaySchedule(LocalDate.of(2020, 1, 1), new ArrayList<>())));
        Ticket ticket3 = new Ticket("ticket for app3", patient);
        ticket3.setAppointment(new Appointment(LocalTime.parse("10:40"), LocalTime.parse("11:00"), AppointmentState.APPOINTMENT,
                new DaySchedule(LocalDate.of(2020, 1, 1), new ArrayList<>())));
        Ticket ticket4 = new Ticket("ticket for app3", patient);
        ticket4.setAppointment(new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:30"), AppointmentState.APPOINTMENT,
                new DaySchedule(LocalDate.of(2020, 1, 1), new ArrayList<>())));

        List<Ticket> ticketList = Arrays.asList(ticket1, ticket2, ticket3, ticket4);

        when(patientDao.getAllTickets(any())).thenReturn(ticketList);

        AddPatientToCommissionDtoRequest dtoRequest = new AddPatientToCommissionDtoRequest(patient.getId(),
                new Integer[]{13, 14}, "100", "01-01-2020", "10:15", "00:20");

        try {
            doctorService.addPatientToCommission(dtoRequest, patient.getId());
            fail();
        } catch (HospitalException ex) {
            assertEquals(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_COMMISSION, ex.getErrorCode());
        }
    }

    @Test
    public void testAddPatientToCommissionFail2() {

        Patient patient = new Patient(2, UserType.PATIENT, "name", "surname",
                "patronymic", "Login", "oldPassword", "email@mail.ru",
                "address", "8-900-000-00-00");

        List<DaySchedule> schedule1 = new LinkedList<>(Collections.singletonList(
                new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                        new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:07"), AppointmentState.FREE),
                        new Appointment(LocalTime.parse("10:07"), LocalTime.parse("10:30"), AppointmentState.APPOINTMENT))))));

        Doctor doctor1 = new Doctor(13, UserType.DOCTOR, "name1", "surname1",
                "patronymic1", "doctorLogin1", "doctorPass1", "хирург",
                "100", schedule1);

        when(doctorDao.getById(anyInt())).thenReturn(doctor1);

        AddPatientToCommissionDtoRequest dtoRequest = new AddPatientToCommissionDtoRequest(patient.getId(),
                new Integer[]{13, 14}, "100", "01-01-2020", "10:15", "00:20");

        try {
            doctorService.addPatientToCommission(dtoRequest, patient.getId());
            fail();
        } catch (HospitalException ex) {
            assertEquals(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_COMMISSION, ex.getErrorCode());
        }

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

}
