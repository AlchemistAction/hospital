package net.thumbtack.school.hospital.service;

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

}
