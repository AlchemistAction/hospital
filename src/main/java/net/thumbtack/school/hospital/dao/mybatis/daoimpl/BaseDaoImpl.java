package net.thumbtack.school.hospital.dao.mybatis.daoimpl;


import net.thumbtack.school.hospital.dao.mybatis.mappers.*;
import net.thumbtack.school.hospital.dao.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

@Component
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

    protected CommissionMapper getCommissionMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(CommissionMapper.class);
    }

    protected CommissionDoctorMapper getCommissionDoctorMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(CommissionDoctorMapper.class);
    }

    protected AppointmentMapper getAppointmentMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AppointmentMapper.class);
    }

    protected PatientMapper getPatientMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(PatientMapper.class);
    }

    protected SessionMapper getSessionMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(SessionMapper.class);
    }

    protected TicketMapper getTicketMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(TicketMapper.class);
    }
}