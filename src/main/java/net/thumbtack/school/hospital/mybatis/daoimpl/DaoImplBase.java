package net.thumbtack.school.hospital.mybatis.daoimpl;


import net.thumbtack.school.hospital.mybatis.mappers.AdminMapper;
import net.thumbtack.school.hospital.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

public class DaoImplBase {

    protected SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    protected AdminMapper getAdminMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AdminMapper.class);
    }



}