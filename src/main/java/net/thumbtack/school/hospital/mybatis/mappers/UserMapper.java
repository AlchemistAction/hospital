package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    @Insert("INSERT INTO user (userType, firstName, lastName, patronymic, login, password) VALUES"
            + "(#{user.userType}, #{user.firstName}, #{user.lastName}, #{user.patronymic}, #{user.login}" +
            ", #{user.password})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    Integer insert(@Param("user") User user);
}
