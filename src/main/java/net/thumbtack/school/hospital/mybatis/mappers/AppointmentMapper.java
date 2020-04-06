package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Doctor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AppointmentMapper {
    @Insert("INSERT INTO appointment (doctor_id, date_of_appointment, timeStart, timeEnd, is_free," +
            " is_locked_for_commission) VALUES (#{doctor.id}, #{appointment.dateOfAppointment}, #{appointment.timeStart},"
            + " #{appointment.timeEnd}, #{appointment.isFree}, #{appointment.isLockedForCommission})")
    Integer insert(@Param("doctor") Doctor doctor, @Param("appointment") Appointment appointment);

    @Select("SELECT date_of_appointment, timeStart, timeEnd, is_free, is_locked_for_commission"
            + " FROM appointment WHERE doctor_id = #{doctor.id}")
    List<Appointment> getByDoctor(Doctor doctor);

}

