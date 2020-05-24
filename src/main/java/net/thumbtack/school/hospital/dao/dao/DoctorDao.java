package net.thumbtack.school.hospital.dao.dao;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Commission;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.validator.exception.HospitalException;

import java.time.LocalDate;
import java.util.List;


public interface DoctorDao {
    Doctor insert(Doctor doctor) throws HospitalException;

    Doctor getById(int id);

    Doctor getByLogin(String login);

    void delete(Doctor doctor);

    void deleteAll();

    List<DaySchedule> insertSchedule(List<DaySchedule> newSchedule, Doctor doctor);

    DaySchedule updateDaySchedule(int doctorId, DaySchedule oldDaySchedule, DaySchedule newDaySchedule);

    Commission insertCommission(Commission commission);

    List<Doctor> getAll();

    List<Doctor> getAllBySpeciality(String speciality);

    void deleteScheduleSinceDate(int id, LocalDate lastDateOfWork);

    void changeAppointmentStateToAppointmentOrCommission(Appointment appointment);

    void changeAllAppointmentsStateToAppointmentOrCommission(List<Appointment> appointments) throws HospitalException;

    void changeAppointmentStateToFree(Appointment appointment);
}
