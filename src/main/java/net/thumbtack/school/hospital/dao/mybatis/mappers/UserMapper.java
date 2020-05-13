package net.thumbtack.school.hospital.dao.mybatis.mappers;

import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.model.UserType;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user (userType, firstName, lastName, patronymic, login, password) VALUES"
            + "(#{user.userType}, #{user.firstName}, #{user.lastName}, #{user.patronymic}, #{user.login}" +
            ", #{user.password})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    Integer insert(@Param("user") User user);

    @Delete("DELETE FROM user WHERE id = #{user.id}")
    void delete(@Param("user") User user);

    @Select("SELECT user.userType FROM user WHERE user.login = #{login}")
    UserType getByLogin(String login);

    @Select("SELECT user.userType FROM user WHERE user.id in (select user_id from session where uuid = #{uuid})")
    UserType getUserTypeBySession(String uuid);

}
