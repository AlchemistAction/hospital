package net.thumbtack.school.hospital.dao.mybatis.mappers;


import net.thumbtack.school.hospital.model.Commission;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface DoctorMapper {

    @Insert("INSERT INTO doctor (id, speciality_id, room_id) SELECT LAST_INSERT_ID(), speciality.id, room.id"
            + " FROM speciality, room WHERE speciality.speciality = #{doctor.speciality}"
            + " and room.room = #{doctor.room}")
    Integer insert(@Param("doctor") Doctor doctor);

    @Select("SELECT user.id, user.userType, firstName, lastName, patronymic, login, password,"
            + " speciality, room FROM user, doctor, speciality, room"
            + " WHERE user.id = #{id} and doctor.id = #{id} and speciality.id = speciality_id and room.id = room_id")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "schedule", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.DayScheduleMapper.getByDoctor",
                            fetchType = FetchType.LAZY)),
            @Result(property = "commissionList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.CommissionMapper.getByDoctor",
                            fetchType = FetchType.LAZY)),
    })
    Doctor getById(int id);

    @Select("SELECT user.id, user.userType, firstName, lastName, patronymic, login, password,"
            + " speciality, room FROM user, doctor, speciality, room"
            + " WHERE user.login = #{login} and doctor.id = user.id and speciality.id = speciality_id and room.id = room_id")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "schedule", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.DayScheduleMapper.getByDoctor",
                            fetchType = FetchType.LAZY)),
            @Result(property = "commissionList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.CommissionMapper.getByDoctor",
                            fetchType = FetchType.LAZY)),
    })
    Doctor getByLogin(String login);

    @Select("SELECT user.id, user.userType, firstName, lastName, patronymic, login, password,"
            + " speciality, room FROM user JOIN doctor on user.id = doctor.id"
            + " JOIN speciality on speciality.id = speciality_id" +
            " JOIN room on room.id = room_id WHERE user.id in" +
            " (select doctor_id from day_schedule where day_schedule.id = #{daySchedule.id})")
    Doctor getByDaySchedule(@Param("daySchedule") DaySchedule daySchedule);

    @Select("SELECT user.id, user.userType, firstName, lastName, patronymic, login, password,"
            + " speciality, room FROM user JOIN doctor on user.id = doctor.id"
            + " JOIN speciality on speciality.id = speciality_id" +
            " JOIN room on room.id = room_id WHERE user.id in" +
            " (select doctor_id from commission_doctor where commission_id = #{commission.id})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "schedule", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.DayScheduleMapper.getByDoctor",
                            fetchType = FetchType.LAZY)),
    })
    List<Doctor> getByCommission(@Param("commission") Commission commission);

    @Select("SELECT user.id, user.userType, firstName, lastName, patronymic, login, password,"
            + " speciality, room FROM user, doctor, speciality, room"
            + " WHERE user.id = doctor.id and speciality.id = speciality_id and room.id = room_id")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "schedule", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.DayScheduleMapper.getByDoctor",
                            fetchType = FetchType.LAZY)),
            @Result(property = "commissionList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.CommissionMapper.getByDoctor",
                            fetchType = FetchType.LAZY)),
    })
    List<Doctor> getAllLazy();

    @Select("SELECT user.id, user.userType, firstName, lastName, patronymic, login, password,"
            + " speciality, room FROM user JOIN doctor on user.id = doctor.id and speciality_id in" +
            " (select id from speciality where speciality = #{speciality}) JOIN speciality" +
            " on speciality = #{speciality} JOIN room on room.id = room_id")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "schedule", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.DayScheduleMapper.getByDoctor",
                            fetchType = FetchType.LAZY)),
            @Result(property = "commissionList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.CommissionMapper.getByDoctor",
                            fetchType = FetchType.LAZY)),
    })
    List<Doctor> getAllBySpeciality(@Param("speciality") String speciality);

    @Delete("DELETE FROM user WHERE id = #{doctor.id}")
    void delete(@Param("doctor") Doctor doctor);

    @Delete("delete from user WHERE userType = 'DOCTOR'")
    void deleteAll();


}
