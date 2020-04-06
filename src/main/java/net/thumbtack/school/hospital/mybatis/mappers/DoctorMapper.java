package net.thumbtack.school.hospital.mybatis.mappers;


import net.thumbtack.school.hospital.model.Doctor;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface DoctorMapper {


    @Insert("INSERT INTO doctor (id, speciality_id, room_id, dateStart, dateEnd)"
            + " SELECT LAST_INSERT_ID(), speciality.id, room.id, #{doctor.dateStart},"
            + " #{doctor.dateEnd} FROM speciality, room WHERE speciality.speciality = #{doctor.speciality}"
            + " and room.room = #{doctor.room}")
    Integer insert(@Param("doctor") Doctor doctor);

    @Select("SELECT user.id, user.userType, firstName, lastName, patronymic, login, password,"
            + " speciality, room, dateStart, dateEnd FROM user, doctor, speciality, room"
            + " WHERE user.id = #{id} and doctor.id = #{id} and speciality.id = speciality_id and room.id = room_id")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "schedule", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.mybatis.mappers.AppointmentMapper.getByDoctor",
                            fetchType = FetchType.LAZY)),
    })
    Doctor getById(int id);

    @Delete("delete from user WHERE userType = 'DOCTOR'")
    void deleteAll();
}
