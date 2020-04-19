package net.thumbtack.school.hospital.daoTest;

import net.thumbtack.school.hospital.model.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestDoctorPatientOperations extends TestBase {

    @Test
    public void testAddPatientToAppointment() {
        try {
            List<DaySchedule> schedule = new LinkedList<>(Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                            new Appointment("10:00", "10:19", AppointmentState.FREE),
                            new Appointment("10:20", "10:39", AppointmentState.FREE)))),
                    new DaySchedule(LocalDate.of(2020, 2, 2), new LinkedList<>(Arrays.asList(
                            new Appointment("11:00", "11:19", AppointmentState.FREE),
                            new Appointment("11:20", "11:39", AppointmentState.FREE))))));
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

            Appointment appointmentFromDb = doctorFromDB.getSchedule().get(0).getAppointmentList().get(0);

            Appointment appointment = new Appointment(appointmentFromDb.getId(), appointmentFromDb.getTimeStart(),
                    appointmentFromDb.getTimeEnd(), AppointmentState.APPOINTMENT,
                    new Ticket("ticketName", patient.getId(), doctor.getId()));

            schedule.get(0).getAppointmentList().remove(0);
            schedule.get(0).getAppointmentList().add(appointment);
            schedule.get(0).getAppointmentList().sort(Comparator.comparing(Appointment::getTimeStart));

            doctor.setSchedule(schedule);

            patientDao.addPersonToAppointment(appointment);

            doctorFromDB = doctorDao.getById(doctor.getId());
            assertEquals(doctor, doctorFromDB);

        } catch (RuntimeException e) {
            fail();
        }
    }
}
