package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.*;
import org.junit.Test;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;

public class TestDoctorOperations extends TestBase {

    @Test
    public void testInsertDoctor() {
        try {
            List<DaySchedule> schedule = Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE))),
                    new DaySchedule(LocalDate.of(2020, 2, 2), Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))));

            Doctor doctor = insertDoctor(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "100", schedule);

            Doctor doctorFromDB = doctorDao.getById(doctor.getId());
            checkDoctorFields(doctor, doctorFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test(expected = RuntimeException.class)
    public void testInsertDoctorWithNullFirstName() {
        Doctor doctor = new Doctor(UserType.DOCTOR, null, "patronymic",
                null, "doctorLogin", "doctorPass", "хирург", "100",
                new ArrayList<>());
        doctorDao.insert(doctor);
    }

    @Test
    public void testGetAllLazy() {
        try {
            List<DaySchedule> schedule1 = Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE))),
                    new DaySchedule(LocalDate.of(2020, 2, 2), Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))));

            Doctor doctor1 = insertDoctor(UserType.DOCTOR, "name1", "surname1",
                    "patronymic1", "doctorLogin1", "doctorPass1", "хирург",
                    "100", schedule1);

            List<DaySchedule> schedule2 = Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 3), Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE))),
                    new DaySchedule(LocalDate.of(2020, 2, 4), Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))));

            Doctor doctor2 = insertDoctor(UserType.DOCTOR, "name2", "surname2",
                    "patronymic2", "doctorLogin2", "doctorPass2", "хирург",
                    "200", schedule2);

            List<DaySchedule> schedule3 = Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 5), Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE))),
                    new DaySchedule(LocalDate.of(2020, 2, 6), Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))));

            Doctor doctor3 = insertDoctor(UserType.DOCTOR, "name3", "surname3",
                    "patronymic3", "doctorLogin3", "doctorPass3", "лор",
                    "300", schedule3);

            List<Doctor> doctorList = Arrays.asList(doctor1, doctor2, doctor3);
            List<Doctor> doctorListFromDb = doctorDao.getAll();

            checkDoctorFields(doctorList.get(0), doctorListFromDb.get(0));
            checkDoctorFields(doctorList.get(1), doctorListFromDb.get(1));
            checkDoctorFields(doctorList.get(2), doctorListFromDb.get(2));

        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testGetAllBySpeciality() {
        try {
            List<DaySchedule> schedule1 = Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE))),
                    new DaySchedule(LocalDate.of(2020, 2, 2), Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))));

            Doctor doctor1 = insertDoctor(UserType.DOCTOR, "name1", "surname1",
                    "patronymic1", "doctorLogin1", "doctorPass1", "хирург",
                    "100", schedule1);

            List<DaySchedule> schedule2 = Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 3), Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE))),
                    new DaySchedule(LocalDate.of(2020, 2, 4), Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))));

            Doctor doctor2 = insertDoctor(UserType.DOCTOR, "name2", "surname2",
                    "patronymic2", "doctorLogin2", "doctorPass2", "хирург",
                    "200", schedule2);

            List<DaySchedule> schedule3 = Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 5), Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE))),
                    new DaySchedule(LocalDate.of(2020, 2, 6), Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))));

            Doctor doctor3 = insertDoctor(UserType.DOCTOR, "name3", "surname3",
                    "patronymic3", "doctorLogin3", "doctorPass3", "лор",
                    "300", schedule3);

            List<Doctor> doctorList = Arrays.asList(doctor1, doctor2);
            List<Doctor> doctorListFromDb = doctorDao.getAllBySpeciality("хирург");

            checkDoctorFields(doctorList.get(0), doctorListFromDb.get(0));
            checkDoctorFields(doctorList.get(1), doctorListFromDb.get(1));

        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testDeleteDoctor() {
        try {
            List<DaySchedule> schedule = Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE))),
                    new DaySchedule(LocalDate.of(2020, 2, 2), Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))));

            Doctor doctor = insertDoctor(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "100", schedule);

            doctorDao.delete(doctor);
            Doctor doctorFromDB = doctorDao.getById(doctor.getId());
            assertNull(doctorFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testInsertNewSchedule() {
        try {
            List<DaySchedule> oldSchedule = new LinkedList<>(Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE)))),
                    new DaySchedule(LocalDate.of(2020, 2, 2), new LinkedList<>(Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))))));

            List<DaySchedule> newSchedule = new LinkedList<>(Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 3), Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE))),
                    new DaySchedule(LocalDate.of(2020, 2, 4), Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE)))));

            Doctor doctor = insertDoctor(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "100", oldSchedule);

            doctorDao.insertSchedule(newSchedule, doctor);

            oldSchedule.addAll(newSchedule);

            doctor.setSchedule(oldSchedule);

            Doctor doctorFromDB = doctorDao.getById(doctor.getId());
            checkDoctorFields(doctor, doctorFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testUpdateDaySchedule() {
        try {
            List<DaySchedule> schedule = new LinkedList<>(Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE)))),
                    new DaySchedule(LocalDate.of(2020, 2, 2), new LinkedList<>(Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))))));

            Doctor doctor = insertDoctor(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "100", schedule);

            DaySchedule updatedDaySchedule = new DaySchedule(LocalDate.of(2020, 1, 1),
                    Arrays.asList(
                            new Appointment(LocalTime.parse("15:00"), LocalTime.parse("15:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("15:20"), LocalTime.parse("15:39"), AppointmentState.FREE)));

            doctorDao.updateDaySchedule(doctor.getId(), doctor.getSchedule().get(0), updatedDaySchedule);

            doctor.getSchedule().remove(0);
            doctor.getSchedule().add(updatedDaySchedule);
            doctor.getSchedule().sort(Comparator.comparing(DaySchedule::getDate));

            Doctor doctorFromDB = doctorDao.getById(doctor.getId());
            doctorFromDB.getSchedule().sort(Comparator.comparing(DaySchedule::getDate));

            checkDoctorFields(doctor, doctorFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testDeleteAppointment() {
        try {
            List<DaySchedule> schedule = Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE)))),
                    new DaySchedule(LocalDate.of(2020, 2, 2), new LinkedList<>(Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE)))));

            Doctor doctor = insertDoctor(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "100", schedule);

            doctorDao.deleteAppointment(doctor.getSchedule().get(1).getAppointmentList().get(0));

            doctor.getSchedule().get(1).getAppointmentList().remove(0);

            Doctor doctorFromDB = doctorDao.getById(doctor.getId());
            checkDoctorFields(doctor, doctorFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testInsertAppointment() {
        try {
            List<DaySchedule> schedule = Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("10:20"), LocalTime.parse("10:39"), AppointmentState.FREE)))),
                    new DaySchedule(LocalDate.of(2020, 2, 2), new LinkedList<>(Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE)))));

            Doctor doctor = insertDoctor(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "100", schedule);

            Appointment neeApp = new Appointment(LocalTime.parse("11:40"), LocalTime.parse("12:00"),
                    AppointmentState.FREE, doctor.getSchedule().get(1));

            Appointment neeAppFromDb = doctorDao.insertAppointment(neeApp);

            doctor.getSchedule().get(1).getAppointmentList().add(neeAppFromDb);

            Doctor doctorFromDB = doctorDao.getById(doctor.getId());
            checkDoctorFields(doctor, doctorFromDB);
        } catch (RuntimeException e) {
            fail();
        }
    }


}
