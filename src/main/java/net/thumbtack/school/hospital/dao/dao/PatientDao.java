package net.thumbtack.school.hospital.dao.dao;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.Ticket;
import net.thumbtack.school.hospital.validator.exception.HospitalException;

import java.util.List;

public interface PatientDao {

    Patient insert(Patient patient);

    Patient getById(int id);

    Patient getByLogin(String login);

    Patient update(Patient patient);

    void deleteAll();

    void addPatientToAppointment(Appointment appointment, Patient patient) throws HospitalException;

    List<Ticket> getAllTickets(Patient patient);
}
