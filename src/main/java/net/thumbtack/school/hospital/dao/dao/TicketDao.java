package net.thumbtack.school.hospital.dao.dao;

import net.thumbtack.school.hospital.model.Ticket;

public interface TicketDao {

    Ticket getByNumber(String ticketNumber);

    void delete(Ticket ticket);
}
