package net.thumbtack.school.hospital.mybatis.mappers;


import net.thumbtack.school.hospital.model.Admin;
import org.apache.ibatis.annotations.*;


public interface AdminMapper {

    @Insert("INSERT INTO admin (id, userType, position) VALUES (LAST_INSERT_ID(), default, #{admin.position})")
    Integer insert(@Param("admin") Admin admin);


    @Select("SELECT user.id, user.userType, firstName, lastName, patronymic, login, password, position FROM user, admin"
            + " WHERE user.id = #{id} and admin.id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
    })
    Admin getById(int id);

    @Delete("DELETE FROM user where id not in (1) and userType = 'ADMIN'")
    void deleteAllExceptOne();


}
