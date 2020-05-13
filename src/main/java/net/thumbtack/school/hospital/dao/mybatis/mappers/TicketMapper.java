package net.thumbtack.school.hospital.dao.mybatis.mappers;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Commission;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.Ticket;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
@Mapper
public interface TicketMapper {

    @Insert("INSERT INTO ticket (number, patient_id, appointment_id)"
            + " VALUES (#{ticket.number}, #{patient.id}, #{appointment.id})")
    @Options(useGeneratedKeys = true, keyProperty = "ticket.id")
    void insertForAppointment(@Param("appointment") Appointment appointment, @Param("ticket") Ticket ticket,
                              @Param("patient") Patient patient);

    @Insert("INSERT INTO ticket (number, patient_id, commission_id)"
            + " VALUES (#{ticket.number}, #{patient.id}, #{commission.id})")
    @Options(useGeneratedKeys = true, keyProperty = "ticket.id")
    void insertForCommission(@Param("commission") Commission commission, @Param("ticket") Ticket ticket,
                             @Param("patient") Patient patient);

    @Select("SELECT id, number FROM ticket where appointment_id = #{appointment.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.PatientMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
            @Result(property = "appointment", column = "id", javaType = Appointment.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.AppointmentMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
    })
    Ticket getByAppointment(@Param("appointment") Appointment appointment);

    @Select("SELECT id, number FROM ticket where commission_id = #{commission.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.PatientMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
            @Result(property = "commission", column = "id", javaType = Commission.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.CommissionMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
    })
    Ticket getByCommission(@Param("commission") Commission commission);

    @Select("SELECT id, number FROM ticket where patient_id = #{patient.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.PatientMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
            @Result(property = "commission", column = "id", javaType = Commission.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.CommissionMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
            @Result(property = "appointment", column = "id", javaType = Appointment.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.AppointmentMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
    })
    List<Ticket> getAllByPatient(@Param("patient") Patient patient);

    @Select("SELECT id, number FROM ticket where number = #{ticketNumber}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.PatientMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
            @Result(property = "commission", column = "id", javaType = Commission.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.CommissionMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
            @Result(property = "appointment", column = "id", javaType = Appointment.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.AppointmentMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
    })
    Ticket getByNumber(String ticketNumber);

    @Delete("DELETE FROM ticket WHERE id = #{ticket.id}")
    void delete(@Param("ticket") Ticket ticket);
}
