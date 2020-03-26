package net.thumbtack.school.hospital.daoTest;

import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.UserType;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestDoctorOperations extends TestBase {

    @Test
    public void testInsertDoctor() {
        try {
            Doctor doctor = insertDoctor(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "200", "20-03", "20-05");
            Doctor doctorFromDB = doctorDao.getById(doctor.getId());
            assertEquals(doctor, doctorFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testInsertDoctorWithSchedule() {
        try {
            Doctor doctor = insertDoctorWithSchedule(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "200", "20-03", "20-05");
            Doctor doctorFromDB = doctorDao.getById(doctor.getId());
            assertEquals(doctor, doctorFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test(expected = RuntimeException.class)
    public void testInsertDoctorWithNullFirstName() {
        Doctor doctor = new Doctor(UserType.DOCTOR, null, "surname", "patronymic",
                "doctorLogin", "doctorPass", "хирург", "200", "20-03",
                "20-05");
        doctorDao.insert(doctor);
    }
}
