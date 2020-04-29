package net.thumbtack.school.hospital.daoTest;

import net.thumbtack.school.hospital.model.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

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
                    "100", schedule);
            Doctor doctorFromDB = doctorDao.getById(doctor.getId());
            checkDoctorFields(doctor, doctorFromDB);

            Patient patient = insertPatient(UserType.PATIENT, "name1", "surname1",
                    "patronymic1", "patientLogin", "patientPass", "email@mail.ru",
                    "address", "8-900-000-00-00");
            Patient patientFromDB = patientDao.getById(patient.getId());
            assertEquals(patient, patientFromDB);

            Appointment appointmentFromDb = doctorFromDB.getSchedule().get(0).getAppointmentList().get(0);

            Appointment appointment = new Appointment(appointmentFromDb.getId(), appointmentFromDb.getTimeStart(),
                    appointmentFromDb.getTimeEnd(), AppointmentState.APPOINTMENT, doctorFromDB.getSchedule().get(0),
                    new Ticket("ticketName", patient));

            schedule.get(0).getAppointmentList().remove(0);
            schedule.get(0).getAppointmentList().add(appointment);
            schedule.get(0).getAppointmentList().sort(Comparator.comparing(Appointment::getTimeStart));

            doctor.setSchedule(schedule);

            patientDao.addPersonToAppointment(doctor.getSchedule().get(0).getAppointmentList().get(0), patient);

            doctorFromDB = doctorDao.getById(doctor.getId());
            checkDoctorFields(doctor, doctorFromDB);

        } catch (RuntimeException e) {
            fail();
        }
    }
}
