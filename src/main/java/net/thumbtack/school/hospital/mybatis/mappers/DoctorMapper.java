package net.thumbtack.school.hospital.mybatis.mappers;


import net.thumbtack.school.hospital.model.Doctor;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface DoctorMapper {

    @Insert("INSERT INTO doctor (id, userType, speciality, room, dateStart, dateEnd)"
            + " VALUES (LAST_INSERT_ID(), default, #{doctor.speciality}, #{doctor.room}, #{doctor.dateStart},"
            + " #{doctor.dateEnd})")
    Integer insert(@Param("doctor") Doctor doctor);

    @Select("SELECT user.id, user.userType, firstName, lastName, patronymic, login, password,"
            + " speciality, room, dateStart, dateEnd FROM user, doctor"
            + " WHERE user.id = #{id} and doctor.id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "weekDaysSchedule", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.mybatis.mappers.DayScheduleMapper.getByDoctor",
                            fetchType = FetchType.LAZY)),
    })
    Doctor getById(int id);

    @Delete("delete from user WHERE userType = 'doctor'")
    void deleteAll();
}
