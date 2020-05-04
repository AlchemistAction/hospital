package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.UserType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPatientOperations extends TestBase {

    @Test
    public void testInsertPatient() {
        try {
            Patient patient = insertPatient(UserType.PATIENT, "name", "surname",
                    "patronymic", "adminLogin", "adminPass", "email@mail.ru",
                    "address", "8-900-000-00-00");
            Patient patientFromDB = patientDao.getById(patient.getId());
            assertEquals(patient, patientFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test(expected = RuntimeException.class)
    public void testInsertPatientWithNullFirstName() {
        Patient patient = insertPatient(UserType.PATIENT, null, "surname",
                "patronymic", "adminLogin", "adminPass", "email@mail.ru",
                "address", "8-900-000-00-00");
        patientDao.insert(patient);
    }

    @Test
    public void testUpdatePatient() {
        try {
            Patient patient = insertPatient(UserType.PATIENT, "name", "surname",
                    "patronymic", "adminLogin", "adminPass", "email@mail.ru",
                    "address", "8-900-000-00-00");

            patient.setFirstName("newName");
            patient.setLastName("newLastName");
            patient.setPatronymic("newPatronymic");
            patient.setPassword("newPassword");
            patient.setEmail("newEmail");
            patient.setAddress("newAddress");
            patient.setPhone("newPhone");

            Patient patientFromDB = patientDao.update(patient);
            assertEquals(patient, patientFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }
}
