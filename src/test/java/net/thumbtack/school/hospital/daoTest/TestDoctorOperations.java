package net.thumbtack.school.hospital.daoTest;

import net.thumbtack.school.hospital.model.Doctor;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestDoctorOperations extends TestBase {

    @Test
    public void testInsertDoctor() {
        try {
            Doctor doctor1 = insertDoctor("doctor", "name2", "sename2",
                    "patronimic2", "doctorLogin", "doctorPass", "хирург",
                    "200", "20-03", "20-05");
            Doctor doctor1FromDB = doctorDao.getById(doctor1.getId());
            assertEquals(doctor1, doctor1FromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testInsertDoctorWithSchedule() {
        try {
            Doctor doctor2 = insertDoctorWithSchedule("doctor", "name2", "sename2",
                    "patronimic2", "doctorLogin", "doctorPass", "хирург",
                    "200", "20-03", "20-05");
            Doctor doctor2FromDB = doctorDao.getById(doctor2.getId());
            assertEquals(doctor2, doctor2FromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test(expected = RuntimeException.class)
    public void testInsertDoctorWithNullFirstName() {
        Doctor doctor1 = new Doctor("doctor", null, "sename2", "patronimic2",
                "doctorLogin", "doctorPass", "хирург", "200", "20-03",
                "20-05");
        doctorDao.insert(doctor1);
    }
}
