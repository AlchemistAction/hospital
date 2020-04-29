package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.Ticket;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

public interface TicketMapper {

    @Insert("INSERT INTO ticket (name, patient_id, appointment_id)"
            + " VALUES (#{ticket.name}, #{patient.id}, #{appointment.id})")
    @Options(useGeneratedKeys = true, keyProperty = "ticket.id")
    Integer insert(@Param("appointment") Appointment appointment, @Param("ticket") Ticket ticket,
                   @Param("patient") Patient patient);

    @Select("SELECT id, name FROM ticket where appointment_id = #{appointment.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.mybatis.mappers.PatientMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
    })
    Ticket getByAppointment(Appointment appointment);
}
