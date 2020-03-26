package net.thumbtack.school.hospital.daoTest;


import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.mybatis.dao.*;
import net.thumbtack.school.hospital.mybatis.daoimpl.*;
import net.thumbtack.school.hospital.mybatis.utils.MyBatisUtils;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotEquals;

public class TestBase {

    protected AdminDao adminDao = new AdminDaoImpl();
    protected DoctorDao doctorDao = new DoctorDaoImpl();
    protected UserDao userDao = new UserDaoImpl();
    protected DayScheduleDao dayScheduleDao = new DayScheduleDaoImpl();
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
        dayScheduleDao.deleteAll();
        doctorDao.deleteAll();
        patientDao.deleteAll();
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
                                  String dateStart, String dateEnd) {
        Doctor doctor = new Doctor(userType, firstName, lastName, patronymic, login, password,
                speciality, room, dateStart, dateEnd);
        doctorDao.insert(doctor);
        assertNotEquals(0, doctor.getId());
        return doctor;
    }

    protected Doctor insertDoctorWithSchedule(UserType userType, String firstName, String lastName, String patronymic,
                                              String login, String password, String speciality, String room,
                                              String dateStart, String dateEnd) {
        Doctor doctor = new Doctor(userType, firstName, lastName, patronymic, login, password,
                speciality, room, dateStart, dateEnd);

        DaySchedule day1 = new DaySchedule("Mon", "10:00", "15:00", "00:30");
        DaySchedule day2 = new DaySchedule("Tue", "11:00", "16:00", "00:40");
        List<DaySchedule> weekDaysSchedule = new ArrayList<>();
        weekDaysSchedule.add(day1);
        weekDaysSchedule.add(day2);

        doctorDao.insertWithSchedule(doctor, weekDaysSchedule);

        doctor.setWeekDaysSchedule(weekDaysSchedule);
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

}
