package net.thumbtack.school.hospital.mybatis.daoimpl;


import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.mybatis.dao.AdminDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AdminDaoImpl extends DaoImplBase implements AdminDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Override
    public Admin insert(Admin admin) {
        LOGGER.debug("DAO insert Admin {}", admin);
        try (SqlSession sqlSession = getSession()) {
            try {
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
            LOGGER.info("Can't get Trainee {}, {}", id, ex);
            throw ex;
        }
    }
//
//    @Override
//    public List<Trainee> getAll() {
//        LOGGER.debug("DAO get all lazy ");
//        try (SqlSession sqlSession = getSession()) {
//            return getTraineeMapper(sqlSession).getAllLazy();
//        } catch (RuntimeException ex) {
//            LOGGER.info("Can't get All Lazy {}", ex);
//            throw ex;
//        }
//    }
//
//    @Override
//    public Trainee update(Trainee trainee) {
//        LOGGER.debug("DAO change Trainee lastname {} {} ", trainee, trainee.getLastName());
//        try (SqlSession sqlSession = getSession()) {
//            try {
//                getTraineeMapper(sqlSession).changeLastName(trainee, trainee.getLastName());
//            } catch (RuntimeException ex) {
//                LOGGER.info("Can't change Trainee Last Name {} {} ", trainee, ex);
//                sqlSession.rollback();
//                throw ex;
//            }
//            sqlSession.commit();
//        }
//        return trainee;
//    }
//
//    @Override
//    public List<Trainee> getAllWithParams(String firstName, String lastName, Integer rating) {
//        LOGGER.debug("DAO get all with params {} {} {}", firstName, lastName, rating);
//        try (SqlSession sqlSession = getSession()) {
//            return getTraineeMapper(sqlSession).getAllWithParams(firstName, lastName, rating);
//        } catch (RuntimeException ex) {
//            LOGGER.info("Can't insert get All with params {}", ex);
//            throw ex;
//        }
//    }
//
//    @Override
//    public void batchInsert(List<Trainee> trainees) {
//        LOGGER.debug("DAO batch insert {}", trainees);
//        try (SqlSession sqlSession = getSession()) {
//            try {
//                getTraineeMapper(sqlSession).batchInsert(trainees);
//            } catch (RuntimeException ex) {
//                LOGGER.info("Can't batch insert Authors {} {} ", trainees, ex);
//                sqlSession.rollback();
//                throw ex;
//            }
//            sqlSession.commit();
//        }
//
//    }
//
//    @Override
//    public void delete(Trainee trainee) {
//        LOGGER.debug("DAO delete Trainee {} ", trainee);
//        try (SqlSession sqlSession = getSession()) {
//            try {
//                getTraineeMapper(sqlSession).delete(trainee);
//            } catch (RuntimeException ex) {
//                LOGGER.info("Can't delete Trainee {} {}", trainee, ex);
//                sqlSession.rollback();
//                throw ex;
//            }
//            sqlSession.commit();
//        }
//    }
//
    @Override
    public void deleteAll() {
        LOGGER.debug("DAO delete all Admins {}");
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdminMapper(sqlSession).deleteAll();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete all Admins {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }

    }
}
