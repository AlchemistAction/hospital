package net.thumbtack.school.hospital.mybatis.mappers;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Patient;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface TicketMapper {

    @Insert("INSERT INTO ticket (ticket, patient_id, appointment_id)"
            + " SELECT #{appointment.ticket}, #{patient.id}, appointment.id"
            + " FROM appointment WHERE timeStart = #{appointment.timeStart} and" +
            " date_of_appointment = #{appointment.dateOfAppointment}")
    Integer insert(@Param("appointment") Appointment appointment, @Param("patient") Patient patient);
}
