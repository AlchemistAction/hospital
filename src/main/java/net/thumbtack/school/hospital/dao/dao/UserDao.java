package net.thumbtack.school.hospital.dao.dao;

import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.model.UserType;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {

    void delete(User user);

    UserType getUserTypeByLogin(String login);

    void setSession(int id, String uuid);

    void endSession(String uuid);

    UserType getUserTypeBySession(String uuid);

    int getIdBySession(String uuid);

    void deleteAllSessions();
}
