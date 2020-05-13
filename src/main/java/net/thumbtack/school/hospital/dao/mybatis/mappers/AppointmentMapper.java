package net.thumbtack.school.hospital.dao.mybatis.mappers;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Ticket;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
@Mapper
public interface AppointmentMapper {

    @Insert({"<script>",
            "INSERT INTO appointment (day_schedule_id, timeStart, timeEnd, state) VALUES",
            "<foreach item='item' collection='list' separator=','>",
            "( #{daySchedule.id}, #{item.timeStart}, #{item.timeEnd}, #{item.state})",
            "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "list.id", keyColumn = "appointment.id")
    void batchInsert(@Param("daySchedule") DaySchedule daySchedule, @Param("list") List<Appointment> appointmentList);

    @Select("SELECT appointment.id, timeStart, timeEnd, state FROM appointment" +
            " where appointment.day_schedule_id = #{daySchedule.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "daySchedule", column = "id", javaType = DaySchedule.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.DayScheduleMapper.getByAppointment",
                            fetchType = FetchType.LAZY)),
            @Result(property = "ticket", column = "id", javaType = Ticket.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.TicketMapper.getByAppointment",
                            fetchType = FetchType.LAZY)),
    })
    List<Appointment> getByDaySchedule(@Param("daySchedule") DaySchedule daySchedule);

    @Select("SELECT appointment.id, timeStart, timeEnd, state FROM appointment" +
            " where appointment.id in (select appointment_id from ticket where ticket.id = #{ticket.id})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "daySchedule", column = "id", javaType = DaySchedule.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.DayScheduleMapper.getByAppointment",
                            fetchType = FetchType.LAZY)),
    })
    Appointment getByTicket(@Param("ticket") Ticket ticket);

    @Update("UPDATE appointment SET state = #{appointment.state} WHERE appointment.id = #{appointment.id}")
    Integer changeState(@Param("appointment") Appointment appointment);

    @Insert({"<script>",
            "<foreach item='item' collection='list' separator=','>",
            "UPDATE appointment",
            "<set>",
                    "state = #{item.state}",
            "</set>",
            "WHERE appointment.id = #{item.id}",
            "</foreach>",
            "</script>"})
    Integer changeAllState(@Param("list") List<Appointment> appointments);
}

