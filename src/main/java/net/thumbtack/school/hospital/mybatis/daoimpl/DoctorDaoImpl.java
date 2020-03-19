package net.thumbtack.school.hospital.mybatis.daoimpl;

import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.mybatis.dao.DoctorDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DoctorDaoImpl extends DaoImplBase implements DoctorDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDaoImpl.class);

    @Override
    public Doctor insert(Doctor doctor) {
        LOGGER.debug("Transactional Doctor Insert {} ", doctor);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(doctor);
                getDoctorMapper(sqlSession).insert(doctor);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Doctor {}, {}", doctor, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return doctor;
    }

    @Override
    public Doctor insertWithSchedule(Doctor doctor, List<DaySchedule> weekDaysSchedule) {
        LOGGER.debug("Transactional Doctor Insert with Schedule {}, {}", doctor, weekDaysSchedule);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(doctor);
                getDoctorMapper(sqlSession).insert(doctor);
                weekDaysSchedule.forEach(daySchedule ->
                        getDayScheduleMapper(sqlSession).insert(doctor, daySchedule));
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Doctor with Schedule {}, {}, {}", doctor, weekDaysSchedule, ex);
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
