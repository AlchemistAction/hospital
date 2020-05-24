package net.thumbtack.school.hospital.dao.dao;

import net.thumbtack.school.hospital.model.Admin;


public interface AdminDao {

    Admin insert(Admin admin);

    Admin getById(int id);

    Admin getByLogin(String login);

    Admin update(Admin admin);

    void deleteAllExceptOne();
}
