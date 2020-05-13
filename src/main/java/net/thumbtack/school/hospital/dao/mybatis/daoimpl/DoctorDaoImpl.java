package net.thumbtack.school.hospital.dao.mybatis.daoimpl;

import net.thumbtack.school.hospital.dao.dao.DoctorDao;
import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Commission;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DoctorDaoImpl extends BaseDaoImpl implements DoctorDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDaoImpl.class);

    @Override
    public Doctor insert(Doctor doctor) {
        LOGGER.debug("DAO Doctor Insert with Schedule {} ", doctor);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(doctor);
                getDoctorMapper(sqlSession).insert(doctor);

                getDayScheduleMapper(sqlSession).batchInsert(doctor, doctor.getSchedule());

                doctor.getSchedule().forEach(daySchedule ->
                        getAppointmentMapper(sqlSession).batchInsert(daySchedule, daySchedule.getAppointmentList()));

            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Doctor with Schedule {}, {}", doctor, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return doctor;
    }

    @Override
    public List<DaySchedule> insertSchedule(List<DaySchedule> newSchedule, Doctor doctor) {
        LOGGER.debug("DAO Insert New Schedule {}, {}", newSchedule, doctor);
        try (SqlSession sqlSession = getSession()) {
            try {
                getDayScheduleMapper(sqlSession).batchInsert(doctor, newSchedule);

                newSchedule.forEach(daySchedule ->
                        getAppointmentMapper(sqlSession).batchInsert(daySchedule, daySchedule.getAppointmentList()));

            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert New Schedule {}, {}, {}", newSchedule, doctor, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return newSchedule;
    }

    @Override
    public DaySchedule updateDaySchedule(int doctorId, DaySchedule oldDaySchedule, DaySchedule newDaySchedule) {
        LOGGER.debug("DAO update DaySchedule {}, {}, {}", doctorId, oldDaySchedule, newDaySchedule);
        try (SqlSession sqlSession = getSession()) {
            try {
                getDayScheduleMapper(sqlSession).delete(oldDaySchedule);

                getDayScheduleMapper(sqlSession).insertOne(doctorId, newDaySchedule);

                getAppointmentMapper(sqlSession).batchInsert(newDaySchedule, newDaySchedule.getAppointmentList());

            } catch (RuntimeException ex) {
                LOGGER.info("Can't update DaySchedule {}, {}, {}, {}", doctorId, newDaySchedule, newDaySchedule, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return newDaySchedule;
    }

    @Override
    public Commission insertCommission(Commission commission) {
        LOGGER.debug("DAO Insert Commission {} ", commission);
        try (SqlSession sqlSession = getSession()) {
            try {

                getCommissionMapper(sqlSession).insert(commission);

                getCommissionDoctoMapper(sqlSession).batchInsert(commission, commission.getDoctorList());

                getTicketMapper(sqlSession).insertForCommission(commission, commission.getTicket(),
                        commission.getTicket().getPatient());

            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Commission {}, {}", commission, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return commission;
    }

    @Override
    public Doctor getById(int id) {
        LOGGER.debug("DAO get Doctor by Id {}", id);
        try (SqlSession sqlSession = getSession()) {
            return getDoctorMapper(sqlSession).getById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Doctor {}, {}", id, ex);
            throw ex;
        }
    }

    @Override
    public Doctor getByLogin(String login) {
        LOGGER.debug("DAO get Doctor by login {}", login);
        try (SqlSession sqlSession = getSession()) {
            return getDoctorMapper(sqlSession).getByLogin(login);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Doctor {}, {}", login, ex);
            throw ex;
        }
    }

    @Override
    public List<Doctor> getAll() {
        LOGGER.debug("DAO get all Doctors lazy");
        try (SqlSession sqlSession = getSession()) {
            return getDoctorMapper(sqlSession).getAllLazy();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get All Doctors Lazy {}", ex);
            throw ex;
        }
    }

    @Override
    public List<Doctor> getAllBySpeciality(String speciality) {
        LOGGER.debug("DAO get all Doctors lazy");
        try (SqlSession sqlSession = getSession()) {
            return getDoctorMapper(sqlSession).getAllBySpeciality(speciality);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get All Doctors Lazy {}", ex);
            throw ex;
        }
    }

    @Override
    public void deleteScheduleSinceDate(int id, LocalDate lastDateOfWork) {
        LOGGER.debug("DAO delete all DaySchedules since {}", lastDateOfWork);
        try (SqlSession sqlSession = getSession()) {
            try {
                getDayScheduleMapper(sqlSession).deleteAllSinceDate(lastDateOfWork);
                getCommissionMapper(sqlSession).deleteAllByDoctorSinceDate(id, lastDateOfWork);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete all DaySchedules since {}, {}", lastDateOfWork, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void changeAppointmentState(Appointment appointment) {
        LOGGER.debug("DAO change Appointment state {}", appointment);
        try (SqlSession sqlSession = getSession()) {
            try {
                getAppointmentMapper(sqlSession).changeState(appointment);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't change Appointment state {}, {}", appointment, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void changeAllAppointmentsState(List<Appointment> appointments) throws HospitalException {
        LOGGER.debug("DAO change all Appointments state {}", appointments);
        try (SqlSession sqlSession = getSession()) {
            try {
                int numberOfChanges = getAppointmentMapper(sqlSession).changeAllState(appointments);
                if (appointments.size() != numberOfChanges) {
                    sqlSession.rollback();
                    throw new HospitalException(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_COMMISSION);
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't change all Appointments state {}, {}", appointments, ex);
                sqlSession.rollback();
                throw ex;
            }

            sqlSession.commit();
        }
    }

    @Override
    public void delete(Doctor doctor) {
        LOGGER.debug("DAO delete Doctor {} ", doctor);
        try (SqlSession sqlSession = getSession()) {
            try {
                getDoctorMapper(sqlSession).delete(doctor);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete Doctor {} {}", doctor, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("DAO delete all Doctors {}");
        try (SqlSession sqlSession = getSession()) {
            try {
                getDoctorMapper(sqlSession).deleteAll();
                getCommissionMapper(sqlSession).deleteAll();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete all Doctors {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }


}
