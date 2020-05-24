package net.thumbtack.school.hospital.dao.mybatis.daoimpl;

import net.thumbtack.school.hospital.dao.dao.PatientDao;
import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.Ticket;
import net.thumbtack.school.hospital.validator.ErrorModel;
import net.thumbtack.school.hospital.validator.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PatientDaoImpl extends BaseDaoImpl implements PatientDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Override
    public Patient insert(Patient patient) {
        LOGGER.debug("DAO Patient Insert {} ", patient);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(patient);
                getPatientMapper(sqlSession).insert(patient);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Patient {}, {}", patient, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return patient;
    }

    @Override
    public Patient getById(int id) {
        LOGGER.debug("DAO get Patient by Id {}", id);
        try (SqlSession sqlSession = getSession()) {
            return getPatientMapper(sqlSession).getById(id);
        }
    }

    @Override
    public Patient getByLogin(String login) {
        LOGGER.debug("DAO get Patient by login {}", login);
        try (SqlSession sqlSession = getSession()) {
            return getPatientMapper(sqlSession).getByLogin(login);
        }
    }

    @Override
    public Patient update(Patient patient) {
        LOGGER.debug("DAO change Patient {} ", patient);
        try (SqlSession sqlSession = getSession()) {
            try {
                getPatientMapper(sqlSession).updatePatient(patient);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't change Patient {} {} ", patient, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return patient;
    }

    @Override
    public void addPatientToAppointment(Appointment appointment, Patient patient) throws HospitalException {
        LOGGER.debug("DAO add Patient to Appointment {}, {}, {}", appointment, appointment.getTicket(), patient);
        try (SqlSession sqlSession = getSession()) {
            try {
                int result = getAppointmentMapper(sqlSession).changeStateToAppointmentOrCommission(appointment);
                if (result != 1) {
                    sqlSession.rollback();
                    throw new HospitalException(new ErrorModel(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_APPOINTMENT,
                            "time",
                            "Appointment on time: " + appointment.getTimeStart() + " is already occupied"));
                }
                getTicketMapper(sqlSession).insertForAppointment(appointment, appointment.getTicket(), patient);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't add Patient to Appointment {}, {}, {}, {}", appointment, appointment.getTicket(),
                        patient, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Ticket> getAllTickets(Patient patient) {
        LOGGER.debug("DAO get all Tickets by Patient {}", patient);
        try (SqlSession sqlSession = getSession()) {
            return getTicketMapper(sqlSession).getAllByPatient(patient);
        }
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("DAO delete all Patients {}");
        try (SqlSession sqlSession = getSession()) {
            try {
                getPatientMapper(sqlSession).deleteAll();
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't delete all Patients {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
