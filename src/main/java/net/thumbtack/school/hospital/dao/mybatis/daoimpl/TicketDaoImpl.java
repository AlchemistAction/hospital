package net.thumbtack.school.hospital.dao.mybatis.daoimpl;

import net.thumbtack.school.hospital.model.Ticket;
import net.thumbtack.school.hospital.dao.dao.TicketDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TicketDaoImpl extends BaseDaoImpl implements TicketDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDaoImpl.class);

    @Override
    public Ticket getByNumber(String ticketNumber) {
        LOGGER.debug("DAO get Ticket by number {}", ticketNumber);
        try (SqlSession sqlSession = getSession()) {
            return getTicketMapper(sqlSession).getByNumber(ticketNumber);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Ticket by number {}, {}", ticketNumber, ex);
            throw ex;
        }
    }

    @Override
    public void delete(Ticket ticket) {
        LOGGER.debug("DAO delete Ticket {} ", ticket);
        try (SqlSession sqlSession = getSession()) {
            try {
                getTicketMapper(sqlSession).delete(ticket);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete Ticket {} {}", ticket, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
