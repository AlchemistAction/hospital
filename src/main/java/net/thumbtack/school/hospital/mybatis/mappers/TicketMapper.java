package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface TicketMapper {

    @Insert("INSERT INTO ticket (name, patient_id, appointment_id)"
            + " VALUES (#{ticket.name}, #{patient.id}, #{appointment.id})")
    @Options(useGeneratedKeys = true, keyProperty = "ticket.id")
    void insertForAppointment(@Param("appointment") Appointment appointment, @Param("ticket") Ticket ticket,
                   @Param("patient") Patient patient);

    @Insert("INSERT INTO ticket (name, patient_id, commission_id)"
            + " VALUES (#{ticket.name}, #{patient.id}, #{commission.id})")
    @Options(useGeneratedKeys = true, keyProperty = "ticket.id")
    void insertForCommission(@Param("commission") Commission commission, @Param("ticket") Ticket ticket,
                                @Param("patient") Patient patient);

    @Select("SELECT id, name FROM ticket where appointment_id = #{appointment.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.mybatis.mappers.PatientMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
            @Result(property = "appointment", column = "id", javaType = Appointment.class,
                    one = @One(select = "net.thumbtack.school.hospital.mybatis.mappers.AppointmentMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
    })
    Ticket getByAppointment(@Param("appointment")Appointment appointment);

    @Select("SELECT id, name FROM ticket where commission_id = #{commission.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.mybatis.mappers.PatientMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
            @Result(property = "commission", column = "id", javaType = Commission.class,
                    one = @One(select = "net.thumbtack.school.hospital.mybatis.mappers.CommissionMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
    })
    Ticket getByCommission(@Param("commission") Commission commission);

    @Select("SELECT id, name FROM ticket where patient_id = #{patient.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patient", column = "id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.mybatis.mappers.PatientMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
            @Result(property = "commission", column = "id", javaType = Commission.class,
                    one = @One(select = "net.thumbtack.school.hospital.mybatis.mappers.CommissionMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
            @Result(property = "appointment", column = "id", javaType = Appointment.class,
                    one = @One(select = "net.thumbtack.school.hospital.mybatis.mappers.AppointmentMapper.getByTicket",
                            fetchType = FetchType.LAZY)),
    })
    List<Ticket> getAllByPatient(@Param("patient") Patient patient);
}
