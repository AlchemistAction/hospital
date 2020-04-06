package net.thumbtack.school.hospital.daoTest;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.UserType;
import org.junit.Test;


import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestDoctorOperations extends TestBase {

    @Test
    public void testInsertDoctor() {
        try {
            List<Appointment> schedule = Arrays.asList(
                    new Appointment("01-01", "10:00", "10:19",
                            true, true),
                    new Appointment("01-01", "10:20", "10:39",
                            true, true),
                    new Appointment("11-01", "11:00", "11:19",
                            true, true),
                    new Appointment("11-01", "11:20", "11:39",
                            true, true));

            Doctor doctor = insertDoctor(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "100", "20-03", "20-05", schedule);
            Doctor doctorFromDB = doctorDao.getById(doctor.getId());
            assertEquals(doctor, doctorFromDB);
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
