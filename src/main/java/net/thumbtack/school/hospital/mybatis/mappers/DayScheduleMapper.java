package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface DayScheduleMapper {

    @Insert("INSERT INTO day_schedule (doctor_id, date_of_appointment)" +
            " VALUES (#{doctor.id}, #{daySchedule.dateOfAppointment})")
    @Options(useGeneratedKeys = true, keyProperty = "daySchedule.id")
    Integer insert(@Param("doctor") Doctor doctor, @Param("daySchedule") DaySchedule daySchedule);

    @Select("SELECT day_schedule.id, date_of_appointment FROM day_schedule WHERE doctor_id = #{doctor.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appointmentList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.mybatis.mappers.AppointmentMapper.getByDaySchedule",
                            fetchType = FetchType.LAZY)),
    })
    List<DaySchedule> getByDoctor(Doctor doctor);
}
