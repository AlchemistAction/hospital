package net.thumbtack.school.hospital.serviceTest;

import net.thumbtack.school.hospital.dto.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.RegisterDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.internal.DayScheduleForDto;
import net.thumbtack.school.hospital.dto.internal.WeekSchedule;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.mybatis.dao.DoctorDao;
import net.thumbtack.school.hospital.mybatis.dao.PatientDao;
import net.thumbtack.school.hospital.mybatis.daoimpl.DoctorDaoImpl;
import net.thumbtack.school.hospital.mybatis.daoimpl.PatientDaoImpl;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.DoctorServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestDoctorService {

    private DoctorDao doctorDao;
    private PatientDao patientDao;
    private DoctorService doctorService;

    @Before
    public void setUp() {
        doctorDao = mock(DoctorDaoImpl.class);
        patientDao = mock(PatientDaoImpl.class);
        doctorService = new DoctorServiceImpl(doctorDao, patientDao);
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
                                new Appointment("10:00", "10:30", AppointmentState.FREE),
                                new Appointment("10:30", "11:00", AppointmentState.FREE))));


        Doctor doctorWithId = new Doctor(3, UserType.DOCTOR, registerDoctorDtoRequest.getFirstName(),
                registerDoctorDtoRequest.getLastName(), registerDoctorDtoRequest.getPatronymic(),
                registerDoctorDtoRequest.getLogin(), registerDoctorDtoRequest.getPassword(),
                registerDoctorDtoRequest.getSpeciality(), registerDoctorDtoRequest.getRoom(),
                registerDoctorDtoRequest.getDateStart(), registerDoctorDtoRequest.getDateEnd(), schedule);

        when(doctorDao.insert(any())).thenReturn(doctorWithId);

        RegisterDoctorDtoResponse registerDoctorDtoResponse = doctorService.registerDoctor(registerDoctorDtoRequest);

        Map<String, Map<String, Patient>> resultMap = new HashMap<>();
        Map<String, Patient> timeMap = new HashMap<>();
        timeMap.put("10:00", null);
        timeMap.put("10:30", null);

        resultMap.put("13-04-2020", timeMap);

        assertEquals(resultMap, registerDoctorDtoResponse.getSchedule());
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
                                new Appointment("10:00", "10:30", AppointmentState.FREE))),
                new DaySchedule(LocalDate.of(2020, 4, 14),
                        Collections.singletonList(
                                new Appointment("11:00", "11:30", AppointmentState.FREE))));


        Doctor doctorWithId = new Doctor(3, UserType.DOCTOR, registerDoctorDtoRequest.getFirstName(),
                registerDoctorDtoRequest.getLastName(), registerDoctorDtoRequest.getPatronymic(),
                registerDoctorDtoRequest.getLogin(), registerDoctorDtoRequest.getPassword(),
                registerDoctorDtoRequest.getSpeciality(), registerDoctorDtoRequest.getRoom(),
                registerDoctorDtoRequest.getDateStart(), registerDoctorDtoRequest.getDateEnd(), schedule);

        when(doctorDao.insert(any())).thenReturn(doctorWithId);

        RegisterDoctorDtoResponse registerDoctorDtoResponse = doctorService.registerDoctor(registerDoctorDtoRequest);

        Map<String, Map<String, Patient>> resultMap = new HashMap<>();

        Map<String, Patient> timeMap1 = new HashMap<>();
        timeMap1.put("10:00", null);
        resultMap.put("13-04-2020", timeMap1);

        Map<String, Patient> timeMap2 = new HashMap<>();
        timeMap2.put("11:00", null);
        resultMap.put("14-04-2020", timeMap2);

        assertEquals(resultMap, registerDoctorDtoResponse.getSchedule());
    }

}
