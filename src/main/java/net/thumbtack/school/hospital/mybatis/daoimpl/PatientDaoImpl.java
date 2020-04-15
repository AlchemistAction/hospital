package net.thumbtack.school.hospital.mybatis.daoimpl;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.mybatis.dao.PatientDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientDaoImpl extends BaseDaoImpl implements PatientDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Override
    public Patient insert(Patient patient) {
        LOGGER.debug("Transactional Patient Insert {} ", patient);
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
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Patient {}, {}", id, ex);
            throw ex;
        }
    }

    @Override
    public void addPersonToAppointment(Appointment appointment, Patient patient) {
        LOGGER.debug("DAO add Patient to Appointment {}, {}", appointment, patient);
        try (SqlSession sqlSession = getSession()) {
            try {
                getAppointmentMapper(sqlSession).changeState(appointment);
                getTicketMapper(sqlSession).insert(appointment, patient);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't add Patient to Appointment {}, {}, {}", appointment, patient, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("DAO delete all Patients {}");
        try (SqlSession sqlSession = getSession()) {
            try {
                getPatientMapper(sqlSession).deleteAll();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete all Patients {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }


}
