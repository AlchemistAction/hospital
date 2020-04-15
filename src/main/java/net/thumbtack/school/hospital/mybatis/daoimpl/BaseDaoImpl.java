package net.thumbtack.school.hospital.mybatis.daoimpl;


import net.thumbtack.school.hospital.mybatis.mappers.*;
import net.thumbtack.school.hospital.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

public class BaseDaoImpl {

    protected SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    protected UserMapper getUserMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(UserMapper.class);
    }

    protected AdminMapper getAdminMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AdminMapper.class);
    }

    protected DoctorMapper getDoctorMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(DoctorMapper.class);
    }

    protected DayScheduleMapper getDayScheduleMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(DayScheduleMapper.class);
    }

    protected AppointmentMapper getAppointmentMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AppointmentMapper.class);
    }

    protected PatientMapper getPatientMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(PatientMapper.class);
    }

    protected TicketMapper getTicketMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(TicketMapper.class);
    }
}