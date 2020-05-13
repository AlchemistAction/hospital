// package net.thumbtack.school.hospital.daoimpl.mybatis;
package net.thumbtack.school.hospital.dao.mybatis.daoimpl;


import net.thumbtack.school.hospital.dao.dao.AdminDao;
import net.thumbtack.school.hospital.model.Admin;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDaoImpl extends BaseDaoImpl implements AdminDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Override
    public Admin insert(Admin admin) {
        LOGGER.debug("DAO Admin Insert {} ", admin);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(admin);
                getAdminMapper(sqlSession).insert(admin);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Admin {}, {}", admin, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return admin;
    }

    @Override
    public Admin getById(int id) {
        LOGGER.debug("DAO get Admin by Id {}", id);
        try (SqlSession sqlSession = getSession()) {
            return getAdminMapper(sqlSession).getById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Admin {}, {}", id, ex);
            throw ex;
        }
    }

    @Override
    public Admin getByLogin(String login) {
        LOGGER.debug("DAO get Admin by login {}", login);
        try (SqlSession sqlSession = getSession()) {
            return getAdminMapper(sqlSession).getByLogin(login);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Admin {}, {}", login, ex);
            throw ex;
        }
    }

    @Override
    public Admin update(Admin admin) {
        LOGGER.debug("DAO change Admin {} ", admin);
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdminMapper(sqlSession).updateAdmin(admin);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't change Admin {} {} ", admin, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return admin;
    }

    @Override
    public void deleteAllExceptOne() {
        LOGGER.debug("DAO delete all Admins except one");
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdminMapper(sqlSession).deleteAllExceptOne();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete all Admins {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
