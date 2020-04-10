package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Patient;
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

    @Delete("delete from user WHERE userType = 'PATIENT'")
    void deleteAll();

}
