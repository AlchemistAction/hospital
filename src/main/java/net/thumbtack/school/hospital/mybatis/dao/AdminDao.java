package net.thumbtack.school.hospital.mybatis.dao;

import net.thumbtack.school.hospital.model.Admin;

public interface AdminDao {

    Admin insert(Admin admin);

    Admin getById(int id);

    void deleteAllExceptOne();
}
