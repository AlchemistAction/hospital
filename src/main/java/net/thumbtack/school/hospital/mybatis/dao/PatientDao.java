package net.thumbtack.school.hospital.mybatis.dao;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface PatientDao {

    Patient insert(Patient patient);

    Patient getById(int id);

    Patient update(Patient patient);

    void deleteAll();

    void addPatientToAppointment(Appointment appointment, Patient patient);

    List<Ticket> getAllTickets(Patient patient);
}
