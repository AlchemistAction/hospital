package net.thumbtack.school.hospital.mybatis.daoimpl;

import net.thumbtack.school.hospital.model.LoginVerificator;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.mybatis.dao.UserDao;
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
                LOGGER.info("Can't delete School {} {}", user, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public LoginVerificator getByLogin(String login) {
        LOGGER.debug("DAO get User ID by Login {}", login);
        try (SqlSession sqlSession = getSession()) {
            return getUserMapper(sqlSession).getByLogin(login);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Doctor {}, {}", login, ex);
            throw ex;
        }
    }
}
