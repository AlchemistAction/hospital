package net.thumbtack.school.hospital.daoTest;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.UserType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestDoctorPatientOperations extends TestBase {

    @Test
    public void testAddPatientToAppointment() {
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
            schedule.sort(Comparator.comparing(Appointment::getTimeStart));

            Doctor doctor = insertDoctor(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "100", "20-03", "20-05", schedule);
            Doctor doctorFromDB = doctorDao.getById(doctor.getId());
            assertEquals(doctor, doctorFromDB);

            Patient patient = insertPatient(UserType.PATIENT, "name", "surname",
                    "patronymic", "adminLogin", "adminPass", "email@mai.ru",
                    "address", "8-900-000-00-00");
            Patient patientFromDB = patientDao.getById(patient.getId());
            assertEquals(patient, patientFromDB);

            Appointment appointment = new Appointment("01-01", "10:00", "10:19",
                    false, true, "fakeTicket");

            List<Appointment> schedule2 = schedule.stream().skip(1).collect(Collectors.toList());
            schedule2.add(appointment);
            schedule2.sort(Comparator.comparing(Appointment::getTimeStart));

            doctor.setSchedule(schedule2);

            patientDao.addPersonToAppointment(appointment, patient);

            doctorFromDB = doctorDao.getById(doctor.getId());
            assertEquals(doctor, doctorFromDB);

        } catch (RuntimeException e) {
            fail();
        }
    }
}
