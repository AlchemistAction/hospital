package net.thumbtack.school.hospital.mybatis.dao;

import net.thumbtack.school.hospital.model.Doctor;


public interface DoctorDao {

    Doctor insert(Doctor doctor);

    Doctor getById(int id);

    void delete(Doctor doctor);

    void deleteAll();
}
