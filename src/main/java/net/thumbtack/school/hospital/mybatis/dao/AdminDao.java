package net.thumbtack.school.hospital.mybatis.dao;

import net.thumbtack.school.hospital.model.Admin;
import org.springframework.stereotype.Component;

@Component
public interface AdminDao {

    Admin insert(Admin admin);

    Admin getById(int id);

    Admin update(Admin admin);

    void deleteAllExceptOne();
}
