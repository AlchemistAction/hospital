package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.dao.dao.*;
import net.thumbtack.school.hospital.dao.mybatis.daoimpl.*;
import net.thumbtack.school.hospital.dao.mybatis.utils.MyBatisUtils;
import net.thumbtack.school.hospital.model.*;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.List;

import static org.junit.Assert.*;

public class TestBase {

    protected AdminDao adminDao = new AdminDaoImpl();
    protected DoctorDao doctorDao = new DoctorDaoImpl();
    protected UserDao userDao = new UserDaoImpl();
    protected TicketDao ticketDao = new TicketDaoImpl();
    protected PatientDao patientDao = new PatientDaoImpl();

    private static boolean setUpIsDone = false;

    @BeforeClass()
    public static void setUp() {
        if (!setUpIsDone) {
            boolean initSqlSessionFactory = MyBatisUtils.initSqlSessionFactory();
            if (!initSqlSessionFactory) {
                throw new RuntimeException("Can't create connection, stop");
            }
            setUpIsDone = true;
        }
    }

    @Before()
    public void clearDatabase() {
        adminDao.deleteAllExceptOne();
        doctorDao.deleteAll();
        patientDao.deleteAll();
        userDao.deleteAllSessions();
    }

    protected Admin insertAdmin(UserType userType, String firstName, String lastName, String patronymic,
                                String login, String password, String position) {
        Admin admin = new Admin(userType, firstName, lastName, patronymic, login, password, position);
        adminDao.insert(admin);
        assertNotEquals(0, admin.getId());
        return admin;
    }

    protected Doctor insertDoctor(UserType userType, String firstName, String lastName, String patronymic,
                                  String login, String password, String speciality, String room,
                                  List<DaySchedule> schedule) {
        Doctor doctor = new Doctor(userType, firstName, lastName, patronymic, login, password,
                speciality, room, schedule);

        doctorDao.insert(doctor);
        assertNotEquals(0, doctor.getId());
        return doctor;
    }

    protected Patient insertPatient(UserType userType, String firstName, String lastName, String patronymic,
                                    String login, String password, String email, String address, String phone) {
        Patient patient = new Patient(userType, firstName, lastName, patronymic,
                login, password, email, address, phone);
        patientDao.insert(patient);
        assertNotEquals(0, patient.getId());
        return patient;
    }

    protected void checkDoctorFields(Doctor doctor1, Doctor doctor2) {
        assertEquals(doctor1.getId(), doctor2.getId());
        assertEquals(doctor1.getUserType(), doctor2.getUserType());
        assertEquals(doctor1.getFirstName(), doctor2.getFirstName());
        assertEquals(doctor1.getLastName(), doctor2.getLastName());
        assertEquals(doctor1.getPatronymic(), doctor2.getPatronymic());
        assertEquals(doctor1.getLogin(), doctor2.getLogin());
        assertEquals(doctor1.getPassword(), doctor2.getPassword());
        assertEquals(doctor1.getSpeciality(), doctor2.getSpeciality());
        assertEquals(doctor1.getRoom(), doctor2.getRoom());
        for (int i = 0; i < doctor1.getSchedule().size(); i++) {
            checkDayScheduleFields(doctor1.getSchedule().get(i), doctor2.getSchedule().get(i));
        }
        if (!( doctor1.getCommissionList() == null) && !( doctor1.getCommissionList() == null)) {
            for (int i = 0; i < doctor1.getCommissionList().size(); i++) {
                checkCommissionFields(doctor1.getCommissionList().get(i), doctor2.getCommissionList().get(i));
            }
        }
    }

    private void checkDayScheduleFields(DaySchedule daySchedule1, DaySchedule daySchedule2) {
        assertEquals(daySchedule1.getId(), daySchedule2.getId());
        assertEquals(daySchedule1.getDate(), daySchedule2.getDate());
        for (int i = 0; i < daySchedule1.getAppointmentList().size(); i++) {
            checkAppointmentFields(daySchedule1.getAppointmentList().get(i), daySchedule2.getAppointmentList().get(i));
        }
    }

    private void checkAppointmentFields(Appointment appointment1, Appointment appointment2) {
        assertEquals(appointment1.getId(), appointment2.getId());
        assertEquals(appointment1.getTimeStart(), appointment2.getTimeStart());
        assertEquals(appointment1.getTimeEnd(), appointment2.getTimeEnd());
        assertEquals(appointment1.getState(), appointment2.getState());
        assertNotNull(appointment2.getDaySchedule());
        if (!(appointment1.getTicket() == null) && !(appointment2.getTicket() == null)) {
            checkTicketFields(appointment1.getTicket(), appointment2.getTicket());
            assertNotNull(appointment2.getTicket().getAppointment());
        }
    }

    private void checkCommissionFields(Commission commission1, Commission commission2) {
        assertEquals(commission1.getId(), commission2.getId());
        assertEquals(commission1.getDate(), commission2.getDate());
        assertEquals(commission1.getTimeStart(), commission2.getTimeStart());
        assertEquals(commission1.getTimeEnd(), commission2.getTimeEnd());
        assertEquals(commission1.getRoom(), commission2.getRoom());
        assertEquals(commission1.getDoctorList().size(), commission2.getDoctorList().size());
        if (!(commission1.getTicket() == null) && !(commission2.getTicket() == null)) {
            checkTicketFields(commission1.getTicket(), commission2.getTicket());
            assertNotNull(commission2.getTicket().getCommission());
        }
    }

    protected void checkTicketFields(Ticket ticket1, Ticket ticket2) {
        assertEquals(ticket1.getId(), ticket2.getId());
        assertEquals(ticket1.getNumber(), ticket2.getNumber());
        assertEquals(ticket1.getPatient(), ticket2.getPatient());
    }

}
