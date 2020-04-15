package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.DaySchedule;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AppointmentMapper {

    @Insert("INSERT INTO appointment (day_schedule_id, timeStart, timeEnd, state)" +
            " VALUES (#{daySchedule.id}, #{appointment.timeStart}, #{appointment.timeEnd}, #{appointment.state})")
    @Options(useGeneratedKeys = true, keyProperty = "appointment.id")
    Integer insert(@Param("daySchedule") DaySchedule daySchedule, @Param("appointment") Appointment appointment);

    @Select("SELECT appointment.id, timeStart, timeEnd, state, ticket FROM appointment" +
            " left join ticket on appointment.id = ticket.appointment_id where" +
            " appointment.day_schedule_id = #{daySchedule.id}")
    List<Appointment> getByDaySchedule(DaySchedule daySchedule);

    @Update("UPDATE appointment SET state = #{appointment.state} WHERE appointment.id = #{appointment.id}")
    void changeState(@Param("appointment") Appointment appointment);
}

