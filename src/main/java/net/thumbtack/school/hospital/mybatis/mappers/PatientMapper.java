package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.Ticket;
import org.apache.ibatis.annotations.*;

public interface PatientMapper {

    @Insert("INSERT INTO patient (id, email, address, phone)" +
            " VALUES (LAST_INSERT_ID(), #{patient.email}, #{patient.address}, #{patient.phone})")
    Integer insert(@Param("patient") Patient patient);

    @Select("SELECT user.id, user.userType, firstName, lastName, patronymic, login, password, email, address, phone"
            + " FROM user, patient"
            + " WHERE user.id = #{id} and patient.id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
    })
    Patient getById(int id);

    @Update("UPDATE user, patient SET firstName = #{patient.firstName}, lastName = #{patient.lastName}," +
            " patronymic = #{patient.patronymic}, password = #{patient.password}, email = #{patient.email}," +
            " address = #{patient.address}, phone = #{patient.phone} WHERE user.id = #{patient.id} ")
    void updatePatient(@Param("patient") Patient patient);

    @Select("SELECT user.id, user.userType, firstName, lastName, patronymic, login, password, email, address, phone"
            + " FROM user, patient"
            + " WHERE user.id in (select patient_id from ticket where ticket.id = #{ticket.id})")
    @Results({
            @Result(property = "id", column = "id"),
    })
    Patient getByTicket(@Param("ticket") Ticket ticket);

    @Delete("delete from user WHERE userType = 'PATIENT'")
    void deleteAll();

}
