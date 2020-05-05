package net.thumbtack.school.hospital.mybatis.dao;

import net.thumbtack.school.hospital.model.User;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {

    void delete(User user);

}
