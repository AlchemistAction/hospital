package net.thumbtack.school.hospital.mybatis.dao;

import net.thumbtack.school.hospital.model.User;

public interface UserDao {

    void update(User user);

    void delete(User user);

}
