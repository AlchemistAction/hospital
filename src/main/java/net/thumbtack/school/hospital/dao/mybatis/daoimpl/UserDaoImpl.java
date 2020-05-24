package net.thumbtack.school.hospital.dao.mybatis.daoimpl;

import net.thumbtack.school.hospital.dao.dao.UserDao;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.model.UserType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Override
    public void delete(User user) {
        LOGGER.debug("DAO delete User {} ", user);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).delete(user);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't delete User {} {}", user, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public UserType getUserTypeByLogin(String login) {
        LOGGER.debug("DAO UserType by Login {}", login);
        try (SqlSession sqlSession = getSession()) {
            return getUserMapper(sqlSession).getByLogin(login);
        }
    }

    @Override
    public void setSession(int id, String uuid) {
        LOGGER.debug("DAO set Session {}, {}", id, uuid);
        try (SqlSession sqlSession = getSession()) {
            try {
                getSessionMapper(sqlSession).insert(id, uuid);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't set Session {}, {}, {}", id, uuid, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void endSession(String uuid) {
        LOGGER.debug("DAO delete SessionId {} ", uuid);
        try (SqlSession sqlSession = getSession()) {
            try {
                getSessionMapper(sqlSession).delete(uuid);
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't delete SessionId {} {}", uuid, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public UserType getUserTypeBySession(String uuid) {
        LOGGER.debug("DAO get UserType by uuid {}", uuid);
        try (SqlSession sqlSession = getSession()) {
            return getUserMapper(sqlSession).getUserTypeBySession(uuid);
        }
    }

    @Override
    public int getIdBySession(String uuid) {
        LOGGER.debug("DAO get ID by uuid {}", uuid);
        try (SqlSession sqlSession = getSession()) {
            return getSessionMapper(sqlSession).getIdBySession(uuid);
        }
    }

    @Override
    public void deleteAllSessions() {
        LOGGER.debug("DAO delete all Sessions");
        try (SqlSession sqlSession = getSession()) {
            try {
                getSessionMapper(sqlSession).deleteAll();
            } catch (RuntimeException ex) {
                LOGGER.debug("Can't delete all Sessions {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
