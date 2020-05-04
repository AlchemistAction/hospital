package net.thumbtack.school.hospital.mybatis.dao;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Commission;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;

import java.util.List;

public interface DoctorDao {
    Doctor insert(Doctor doctor);

    Doctor getById(int id);

    void delete(Doctor doctor);

    void deleteAll();

    List<DaySchedule> insertSchedule(List<DaySchedule> newSchedule, Doctor doctor);

    DaySchedule updateDaySchedule(int doctorId, DaySchedule oldDaySchedule, DaySchedule newDaySchedule);

    void deleteAppointment(Appointment appointment);

    Appointment insertAppointment(Appointment appointment);

    Commission insertCommission(Commission commission);

    List<Doctor> getAll();

    List<Doctor> getAllBySpeciality(String speciality);
}
