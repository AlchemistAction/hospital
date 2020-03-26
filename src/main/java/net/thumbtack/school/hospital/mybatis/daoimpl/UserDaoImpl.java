package net.thumbtack.school.hospital.mybatis.daoimpl;

import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.mybatis.dao.UserDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDaoImpl extends DaoImplBase implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Override
    public void update(User user) {
        LOGGER.debug("DAO update User {} {}", user, user.getPassword());
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).updateUser(user, user.getPassword());
            } catch (RuntimeException ex) {
                LOGGER.info("Can't update User password {} {} ", user, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void delete(User user) {
        LOGGER.debug("DAO delete User {} ", user);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).delete(user);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete School {} {}", user, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
