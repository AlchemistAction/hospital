package net.thumbtack.school.hospital.dao.mybatis.mappers;

import net.thumbtack.school.hospital.model.Commission;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Ticket;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.time.LocalDate;
import java.util.List;
@Mapper
public interface CommissionMapper {

    @Insert("INSERT INTO commission (date, timeStart, timeEnd, room_id)" +
            "SELECT #{commission.date}, #{commission.timeStart}, #{commission.timeEnd}, room.id" +
            " FROM room WHERE room.room = #{commission.room}")
    @Options(useGeneratedKeys = true, keyProperty = "commission.id")
    Integer insert(@Param("commission") Commission commission);

    @Select("SELECT commission.id, commission.date, commission.timeStart, commission.timeEnd, room" +
            " FROM commission JOIN room on room.id = commission.room_id WHERE commission.id" +
            " in (select commission_id from commission_doctor where doctor_id = #{doctor.id})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "doctorList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.DoctorMapper.getByCommission",
                            fetchType = FetchType.LAZY)),
            @Result(property = "ticket", column = "id", javaType = Ticket.class,
                    one = @One(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.TicketMapper.getByCommission",
                            fetchType = FetchType.LAZY)),
    })
    Commission getByDoctor(@Param("doctor") Doctor doctor);

    @Select("SELECT commission.id, commission.date, commission.timeStart, commission.timeEnd, room" +
            " FROM commission JOIN room on room.id = commission.room_id WHERE commission.id" +
            " in (select commission_id from ticket where ticket.id = #{ticket.id})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "doctorList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.dao.mybatis.mappers.DoctorMapper.getByCommission",
                            fetchType = FetchType.LAZY)),
    })
    Commission getByTicket(@Param("ticket") Ticket ticket);

    @Delete("delete from commission")
    void deleteAll();

    @Delete("delete from commission where commission.id in " +
            "(select commission_id from commission_doctor where doctor_id = #{id})" +
            " and date >= #{lastDateOfWork}")
    void deleteAllByDoctorSinceDate(@Param("id") int id, @Param("lastDateOfWork") LocalDate lastDateOfWork);

}
