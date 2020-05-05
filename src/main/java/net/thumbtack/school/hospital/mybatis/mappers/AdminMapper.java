package net.thumbtack.school.hospital.mybatis.mappers;


import net.thumbtack.school.hospital.model.Admin;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AdminMapper {

    @Insert("INSERT INTO admin (id, position) VALUES (LAST_INSERT_ID(), #{admin.position})")
    Integer insert(@Param("admin") Admin admin);


    @Select("SELECT user.id, user.userType, firstName, lastName, patronymic, login, password, position FROM user, admin"
            + " WHERE user.id = #{id} and admin.id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
    })
    Admin getById(int id);

    @Update("UPDATE user, admin SET firstName = #{admin.firstName}, lastName = #{admin.lastName}," +
            " patronymic = #{admin.patronymic}, password = #{admin.password}, position = #{admin.position}" +
            " WHERE user.id = #{admin.id} ")
    void updateAdmin(@Param("admin") Admin admin);

    @Delete("DELETE FROM user where id not in (1) and userType = 'ADMIN'")
    void deleteAllExceptOne();


}
