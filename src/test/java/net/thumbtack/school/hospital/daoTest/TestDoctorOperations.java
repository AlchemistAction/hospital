package net.thumbtack.school.hospital.daoTest;

import net.thumbtack.school.hospital.model.*;
import org.junit.Test;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

import static org.junit.Assert.*;

public class TestDoctorOperations extends TestBase {

    @Test
    public void testInsertDoctor() {
        try {
            List<DaySchedule> schedule = Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), Arrays.asList(
                            new Appointment("10:00", "10:19", AppointmentState.FREE),
                            new Appointment("10:20", "10:39", AppointmentState.FREE))),
                    new DaySchedule(LocalDate.of(2020, 2, 2), Arrays.asList(
                            new Appointment("11:00", "11:19", AppointmentState.FREE),
                            new Appointment("11:20", "11:39", AppointmentState.FREE))));

            Doctor doctor = insertDoctor(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "100", "20-03", "20-05", schedule);
            Doctor doctorFromDB = doctorDao.getById(doctor.getId());
            assertEquals(doctor, doctorFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testDeleteDoctor() {
        try {
            Doctor doctor = insertDoctor(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "100", "20-03", "20-05", new ArrayList<>());
            Doctor doctorFromDB = doctorDao.getById(doctor.getId());
            assertEquals(doctor, doctorFromDB);
            doctorDao.delete(doctor);
            doctorFromDB = doctorDao.getById(doctor.getId());
            assertNull(doctorFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test(expected = RuntimeException.class)
    public void testInsertDoctorWithNullFirstName() {
        Doctor doctor = new Doctor(UserType.DOCTOR, null, "surname", "patronymic",
                "doctorLogin", "doctorPass", "хирург", "100", "20-03",
                "20-05");
        doctorDao.insert(doctor);
    }
}
