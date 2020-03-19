package net.thumbtack.school.hospital.mybatis.dao;

import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;

import java.util.List;

public interface DoctorDao {

    Doctor insert(Doctor doctor);

    Doctor insertWithSchedule(Doctor doctor, List<DaySchedule> weekDaysSchedule);

    Doctor getById(int id);

    void deleteAll();
}
