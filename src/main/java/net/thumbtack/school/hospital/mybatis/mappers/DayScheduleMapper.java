package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DayScheduleMapper {

    @Insert("INSERT INTO day_schedule (doctor_id, weekDay, timeStart, timeEnd, duration) VALUES (#{doctor.id}," +
            " #{daySchedule.weekDay}, #{daySchedule.timeStart}, #{daySchedule.timeEnd}, #{daySchedule.duration})")
    Integer insert(@Param("doctor") Doctor doctor, @Param("daySchedule") DaySchedule daySchedule);


    @Select("SELECT * FROM day_schedule WHERE doctor_id = #{doctor.id}")
    List<DaySchedule> getByDoctor(Doctor doctor);

    @Delete("delete from day_schedule")
    void deleteAll();
}
