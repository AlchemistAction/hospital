package net.thumbtack.school.hospital.mybatis.daoimpl;

import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.mybatis.dao.DoctorDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DoctorDaoImpl extends BaseDaoImpl implements DoctorDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDaoImpl.class);

    @Override
    public Doctor insert(Doctor doctor) {
        LOGGER.debug("Transactional Doctor Insert with Schedule{} ", doctor);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(doctor);
                getDoctorMapper(sqlSession).insert(doctor);
                doctor.getSchedule().forEach(appointment -> getAppointmentMapper(sqlSession).insert(doctor, appointment));
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
    public void deleteAll() {
        LOGGER.debug("DAO delete all Doctors {}");
        try (SqlSession sqlSession = getSession()) {
            try {
                getDoctorMapper(sqlSession).deleteAll();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete all Doctors {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
