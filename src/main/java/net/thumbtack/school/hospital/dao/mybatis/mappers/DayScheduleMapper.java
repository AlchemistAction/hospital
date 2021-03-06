package net.thumbtack.school.hospital.dao.mybatis.mappers;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DayScheduleMapper {

    @Insert("INSERT INTO day_schedule (doctor_id, date) VALUES (#{doctorId}, #{daySchedule.date})")
    @Options(useGeneratedKeys = true, keyProperty = "daySchedule.id")
    void insertOne(@Param("doctorId") int doctorId, @Param("daySchedule") DaySchedule daySchedule);

    @Insert({"<script>",
            "INSERT INTO day_schedule (doctor_id, date) VALUES",
            "<foreach item='item' collection='list' separator=','>",
            "( #{doctor.id}, #{item.date})",
            "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "list.id", keyColumn = "daySchedule.id")
    void batchInsert(@Param("doctor") Doctor doctor, @Param("list") List<DaySchedule> schedule);

    @Select("SELECT day_schedule.id, `date` FROM day_schedule WHERE doctor_id = #{doctor.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "doctor", column = "id", javaType = Doctor.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.DoctorMapper.getByDaySchedule",
                            fetchType = FetchType.LAZY)),
            @Result(property = "appointmentList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.AppointmentMapper.getByDaySchedule",
                            fetchType = FetchType.LAZY)),
    })
    List<DaySchedule> getByDoctor(@Param("doctor") Doctor doctor);

    @Select("SELECT day_schedule.id, date FROM day_schedule WHERE day_schedule.id in" +
            " (select day_schedule_id from appointment where appointment.id = #{appointment.id})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "doctor", column = "id", javaType = Doctor.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.DoctorMapper.getByDaySchedule",
                            fetchType = FetchType.LAZY)),
    })
    DaySchedule getByAppointment(@Param("appointment") Appointment appointment);

    @Delete("DELETE FROM day_schedule WHERE id = #{daySchedule.id}")
    void delete(@Param("daySchedule") DaySchedule daySchedule);

    @Delete("DELETE FROM day_schedule WHERE date >= #{lastDateOfWork} and doctor_id = #{id}")
    void deleteAllSinceDate(@Param("id") int id, @Param("lastDateOfWork") LocalDate lastDateOfWork);
}
