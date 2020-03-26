package net.thumbtack.school.hospital.mybatis.dao;

import net.thumbtack.school.hospital.model.Patient;

public interface PatientDao {

    Patient insert(Patient patient);

    Patient getById(int id);

    void deleteAll();
}
