package net.thumbtack.school.hospital.mybatis.daoimpl;


import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.mybatis.dao.AdminDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AdminDaoImpl extends DaoImplBase implements AdminDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Override
    public Admin insert(Admin admin) {
        LOGGER.debug("Transactional Admin Insert {} ", admin);
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
    public void deleteAllExceptOne() {
        LOGGER.debug("DAO delete all Admins except one {}");
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
