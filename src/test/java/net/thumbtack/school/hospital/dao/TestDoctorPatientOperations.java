package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.fail;

public class TestDoctorPatientOperations extends TestBase {

    @Test
    public void testAddPatientToAppointment() {

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
            Doctor doctorFromDB = doctorDao.getById(doctor.getId());

            Patient patient = insertPatient(UserType.PATIENT, "name1", "surname1",
                    "patronymic1", "patientLogin", "patientPass", "email@mail.ru",
                    "address", "8-900-000-00-00");

            Appointment appointmentFromDb = doctorFromDB.getSchedule().get(0).getAppointmentList().get(0);

            Appointment appointment = new Appointment(appointmentFromDb.getId(), appointmentFromDb.getTimeStart(),
                    appointmentFromDb.getTimeEnd(), AppointmentState.APPOINTMENT, doctorFromDB.getSchedule().get(0),
                    new Ticket("ticketName", patient));

            schedule.get(0).getAppointmentList().remove(0);
            schedule.get(0).getAppointmentList().add(appointment);
            schedule.get(0).getAppointmentList().sort(Comparator.comparing(Appointment::getTimeStart));

            doctor.setSchedule(schedule);

            patientDao.addPatientToAppointment(doctor.getSchedule().get(0).getAppointmentList().get(0), patient);

            doctorFromDB = doctorDao.getById(doctor.getId());
            checkDoctorFields(doctor, doctorFromDB);

        } catch (RuntimeException | HospitalException e) {
            fail();
        }
    }

    @Test
    public void testCancelAppointment() {

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
            Doctor doctorFromDB = doctorDao.getById(doctor.getId());

            Patient patient = insertPatient(UserType.PATIENT, "name1", "surname1",
                    "patronymic1", "patientLogin", "patientPass", "email@mail.ru",
                    "address", "8-900-000-00-00");

            Appointment appointmentFromDb = doctorFromDB.getSchedule().get(0).getAppointmentList().get(0);

            Appointment appointment = new Appointment(appointmentFromDb.getId(), appointmentFromDb.getTimeStart(),
                    appointmentFromDb.getTimeEnd(), AppointmentState.APPOINTMENT, doctorFromDB.getSchedule().get(0),
                    new Ticket("ticketName", patient));

            schedule.get(0).getAppointmentList().remove(0);
            schedule.get(0).getAppointmentList().add(appointment);
            schedule.get(0).getAppointmentList().sort(Comparator.comparing(Appointment::getTimeStart));

            doctor.setSchedule(schedule);

            patientDao.addPatientToAppointment(doctor.getSchedule().get(0).getAppointmentList().get(0), patient);

            Ticket ticketFromDb = ticketDao.getByNumber(appointment.getTicket().getNumber());

            ticketDao.delete(ticketFromDb);

            Appointment appointmentWithoutTicket = new Appointment(appointmentFromDb.getId(), appointmentFromDb.getTimeStart(),
                    appointmentFromDb.getTimeEnd(), AppointmentState.APPOINTMENT, doctorFromDB.getSchedule().get(0));

            schedule.get(0).getAppointmentList().remove(0);
            schedule.get(0).getAppointmentList().add(appointmentWithoutTicket);
            schedule.get(0).getAppointmentList().sort(Comparator.comparing(Appointment::getTimeStart));

            doctor.setSchedule(schedule);

            doctorFromDB = doctorDao.getById(doctor.getId());
            checkDoctorFields(doctor, doctorFromDB);

        } catch (RuntimeException | HospitalException e) {
            fail();
        }
    }

    @Test
    public void testAddPatientToCommission() {

        try {
            Patient patient = insertPatient(UserType.PATIENT, "name1", "surname1",
                    "patronymic1", "patientLogin", "patientPass", "email@mail.ru",
                    "address", "8-900-000-00-00");

            Ticket ticket = new Ticket("ticketName", patient);

            List<DaySchedule> schedule1 = new LinkedList<>(Collections.singletonList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:30"), AppointmentState.COMMISSION),
                            new Appointment(LocalTime.parse("10:30"), LocalTime.parse("11:00"), AppointmentState.FREE))))));

            Doctor doctor1 = insertDoctor(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "100", schedule1);
            doctor1.setCommissionList(new ArrayList<>());

            List<DaySchedule> schedule2 = new LinkedList<>(Collections.singletonList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:30"), AppointmentState.COMMISSION),
                            new Appointment(LocalTime.parse("10:30"), LocalTime.parse("11:00"), AppointmentState.FREE))))));

            Doctor doctor2 = insertDoctor(UserType.DOCTOR, "name2", "surname2",
                    "patronymic2", "doctorLogin2", "doctorPass2", "хирург",
                    "200", schedule2);
            doctor2.setCommissionList(new ArrayList<>());

            List<Doctor> doctorList = Arrays.asList(doctor1, doctor2);

            Commission commission = new Commission(LocalDate.of(2020, 1, 1),
                    LocalTime.parse("10:10"), LocalTime.parse("10:20"), doctor1.getRoom(), doctorList, ticket);

            Commission commissionFromDb = doctorDao.insertCommission(commission);

            doctorList.forEach(doctor -> doctor.getCommissionList().add(commissionFromDb));

            List<Doctor> doctorListFromDb = doctorDao.getAll();

            checkDoctorFields(doctorList.get(0), doctorListFromDb.get(0));
            checkDoctorFields(doctorList.get(1), doctorListFromDb.get(1));

        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testGetAllTicketsByPatient() {

        try {
            Patient patient = insertPatient(UserType.PATIENT, "name1", "surname1",
                    "patronymic1", "patientLogin", "patientPass", "email@mail.ru",
                    "address", "8-900-000-00-00");

            Ticket ticket1 = new Ticket("ticketName for Commission", patient);
            Ticket ticket2 = new Ticket("ticketName for Appointment", patient);

            List<DaySchedule> schedule1 = new LinkedList<>(Arrays.asList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:30"), AppointmentState.COMMISSION),
                            new Appointment(LocalTime.parse("10:30"), LocalTime.parse("11:00"), AppointmentState.FREE)))),
                    new DaySchedule(LocalDate.of(2020, 2, 2), new LinkedList<>(Arrays.asList(
                            new Appointment(LocalTime.parse("11:00"), LocalTime.parse("11:19"), AppointmentState.FREE),
                            new Appointment(LocalTime.parse("11:20"), LocalTime.parse("11:39"), AppointmentState.FREE))))));

            Doctor doctor1 = insertDoctor(UserType.DOCTOR, "name", "surname",
                    "patronymic", "doctorLogin", "doctorPass", "хирург",
                    "100", schedule1);

            List<DaySchedule> schedule2 = new LinkedList<>(Collections.singletonList(
                    new DaySchedule(LocalDate.of(2020, 1, 1), new LinkedList<>(Arrays.asList(
                            new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:30"), AppointmentState.COMMISSION),
                            new Appointment(LocalTime.parse("10:30"), LocalTime.parse("11:00"), AppointmentState.FREE))))));

            Doctor doctor2 = insertDoctor(UserType.DOCTOR, "name2", "surname2",
                    "patronymic2", "doctorLogin2", "doctorPass2", "лор",
                    "200", schedule2);

            Commission commission = new Commission(LocalDate.of(2020, 1, 1),
                    LocalTime.parse("10:10"), LocalTime.parse("10:20"), doctor1.getRoom(),
                    Arrays.asList(doctor1, doctor2), ticket1);

            Commission commissionFromDb = doctorDao.insertCommission(commission);

            Doctor doctor1FromDB = doctorDao.getById(doctor1.getId());

            Appointment appointmentFromDb = doctor1FromDB.getSchedule().get(0).getAppointmentList().get(1);

            DaySchedule daySchedule = doctor1FromDB.getSchedule().get(0);

            Appointment appointment = new Appointment(appointmentFromDb.getId(), appointmentFromDb.getTimeStart(),
                    appointmentFromDb.getTimeEnd(), AppointmentState.APPOINTMENT, daySchedule, ticket2);

            schedule1.get(0).getAppointmentList().remove(1);
            schedule1.get(0).getAppointmentList().add(appointment);
            schedule1.get(0).getAppointmentList().sort(Comparator.comparing(Appointment::getTimeStart));

            doctor1.setSchedule(schedule1);

            patientDao.addPatientToAppointment(appointment, patient);

            ticket1.setCommission(commissionFromDb);
            ticket2.setAppointment(appointment);

            List<Ticket> ticketList = Arrays.asList(ticket1, ticket2);

            List<Ticket> ticketListFromDb = patientDao.getAllTickets(patient);

            checkTicketFields(ticketList.get(0), ticketListFromDb.get(0));
            checkTicketFields(ticketList.get(1), ticketListFromDb.get(1));

        } catch (RuntimeException | HospitalException e) {
            fail();
        }
    }
}
