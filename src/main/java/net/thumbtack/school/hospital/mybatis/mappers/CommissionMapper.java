package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Commission;
import net.thumbtack.school.hospital.model.Ticket;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface CommissionMapper {

    @Insert("INSERT INTO commission (room_id) SELECT room.id FROM room WHERE room.room = #{commission.room}")
    @Options(useGeneratedKeys = true, keyProperty = "commission.id")
    Integer insert(@Param("commission") Commission commission);

    @Select("SELECT commission.id, room FROM commission, room where room.id = room_id and commission.id" +
            " in (select commission_id from commission_appointment where appointment_id = #{appointment.id})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appointmentList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.mybatis.mappers.AppointmentMapper.getByCommission",
                            fetchType = FetchType.LAZY)),
            @Result(property = "ticket", column = "id", javaType = Ticket.class,
                    one = @One(select = "net.thumbtack.school.hospital.mybatis.mappers.TicketMapper.getByCommission",
                            fetchType = FetchType.LAZY)),
    })
    Commission getByAppointment(@Param("appointment")Appointment appointment);

    @Select("SELECT commission.id, room FROM commission, room where room.id = room_id and commission.id" +
            " in (select commission_id from ticket where ticket.id = #{ticket.id})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appointmentList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.mybatis.mappers.AppointmentMapper.getByCommission",
                            fetchType = FetchType.LAZY)),
    })
    Commission getByTicket(@Param("ticket") Ticket ticket);

    @Delete("delete from commission")
    void deleteAll();

    @Delete("delete from commission where commission.id in" +
            " (select commission_id from commission_appointment where appointment_id in" +
            "(select appointment.id from appointment where day_schedule_id in" +
            "(select day_schedule.id from day_schedule where doctor_id = #{id})))")
    void deleteByDoctor(int id);

}
