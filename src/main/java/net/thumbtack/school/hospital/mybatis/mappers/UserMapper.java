package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.annotations.*;

public interface UserMapper {

    @Insert("INSERT INTO user (userType, firstName, lastName, patronymic, login, password) VALUES"
            + "(#{user.userType}, #{user.firstName}, #{user.lastName}, #{user.patronymic}, #{user.login}" +
            ", #{user.password})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    Integer insert(@Param("user") User user);

    @Update("UPDATE user SET password = #{password} WHERE id = #{user.id} ")
    void updateUser(@Param("user") User user, @Param("password") String password);

    @Delete("DELETE FROM user WHERE id = #{user.id}")
    void delete(@Param("user") User user);
}
