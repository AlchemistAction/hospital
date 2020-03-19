package net.thumbtack.school.hospital.mybatis.daoimpl;

import net.thumbtack.school.hospital.mybatis.dao.DayScheduleDao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DayScheduleDaoImpl extends DaoImplBase implements DayScheduleDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DayScheduleDaoImpl.class);

    @Override
    public void deleteAll() {
        LOGGER.debug("DAO delete all Schedules {}");
        try (SqlSession sqlSession = getSession()) {
            try {
                getDayScheduleMapper(sqlSession).deleteAll();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete all Schedules {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
