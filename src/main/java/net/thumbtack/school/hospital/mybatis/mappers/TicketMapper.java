package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Ticket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TicketMapper {

    @Insert("INSERT INTO ticket (name, patient_id, doctor_id, appointment_id)"
            + " VALUES (#{ticket.name}, #{ticket.patientId}, #{ticket.doctorId}, #{appointment.id})")
    @Options(useGeneratedKeys = true, keyProperty = "ticket.id")
    Integer insert(@Param("appointment") Appointment appointment, @Param("ticket") Ticket ticket);

    @Select("SELECT id, name, patient_id, doctor_id FROM ticket where appointment_id = #{appointment.id}")
    Ticket getByAppointment(Appointment appointment);
}
