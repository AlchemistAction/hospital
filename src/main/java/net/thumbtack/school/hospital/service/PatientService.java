package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.request.AddPatientToAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.AddPatientToAppointmentDtoResponse;
import net.thumbtack.school.hospital.dto.response.RegisterPatientDtoResponse;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.model.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.model.exception.HospitalException;

import net.thumbtack.school.hospital.mybatis.dao.DoctorDao;
import net.thumbtack.school.hospital.mybatis.dao.PatientDao;
import net.thumbtack.school.hospital.mybatis.dao.UserDao;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PatientService {

    private PatientDao patientDao;
    private UserDao userDao;
    private DoctorDao doctorDao;
    private ModelMapper modelMapper = new ModelMapper();

    public PatientService(PatientDao patientDao, DoctorDao doctorDao, UserDao userDao) {
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
        this.userDao = userDao;
    }

    public RegisterPatientDtoResponse registerPatient(RegisterPatientDtoRequest registerPatientDtoRequest) {
        Patient patient = modelMapper.map(registerPatientDtoRequest, Patient.class);

        patient = patientDao.insert(patient);

        return modelMapper.map(patient, RegisterPatientDtoResponse.class);
    }

    public RegisterPatientDtoResponse updatePatient(UpdatePatientDtoRequest updatePatientDtoRequest, int id) {

        Patient patient = patientDao.getById(id);

        patient.setPassword(updatePatientDtoRequest.getNewPassword());

        userDao.update(patient);

        return modelMapper.map(patient, RegisterPatientDtoResponse.class);
    }

    public AddPatientToAppointmentDtoResponse addPatientToAppointment(
            AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest, int patientId) throws HospitalException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateOfAppointment = LocalDate.parse(addPatientToAppointmentDtoRequest.getDate(), formatter);

        Patient patient = patientDao.getById(patientId);

        AddPatientToAppointmentDtoResponse result = null;

        if (addPatientToAppointmentDtoRequest.getSpeciality() == null) {

            Doctor doctor = doctorDao.getById(addPatientToAppointmentDtoRequest.getDoctorId());

            for (DaySchedule daySchedule : doctor.getSchedule()) {

                if (daySchedule.getDate().equals(dateOfAppointment)) {

                    for (Appointment appointment : daySchedule.getAppointmentList()) {

                        if (appointment.getTimeStart().equals(addPatientToAppointmentDtoRequest.getTime()) &&
                                appointment.getState().equals(AppointmentState.FREE)) {

                            String ticketName = "D" + doctor.getId() +
                                    daySchedule.getDate().toString().replace("-", "") +
                                    appointment.getTimeStart().replace(":", "");

                            Ticket ticket = new Ticket(ticketName, patient);

                            appointment.setTicket(ticket);

                            patientDao.addPersonToAppointment(appointment, patient);

                            result = new AddPatientToAppointmentDtoResponse(appointment.getTicket().getName(),
                                    doctor.getId(), doctor.getFirstName(),
                                    doctor.getLastName(), doctor.getPatronymic(),
                                    doctor.getSpeciality(), doctor.getRoom(),
                                    dateOfAppointment.toString(), appointment.getTimeStart());
                        }
                    }
                }
            }

        } else {

            List<Doctor> doctorList = doctorDao.getAllBySpeciality(addPatientToAppointmentDtoRequest.getSpeciality());
            for (Doctor doctor : doctorList) {

                if (doctor.getSpeciality().equals(addPatientToAppointmentDtoRequest.getSpeciality())) {
                    for (DaySchedule daySchedule : doctor.getSchedule()) {

                        if (daySchedule.getDate().equals(dateOfAppointment)) {

                            for (Appointment appointment : daySchedule.getAppointmentList()) {

                                if ((appointment.getTimeStart().equals(addPatientToAppointmentDtoRequest.getTime()) &&
                                        appointment.getState().equals(AppointmentState.FREE))) {

                                    String ticketName = "D" + doctor.getId() +
                                            daySchedule.getDate().toString().replace("-", "") +
                                            appointment.getTimeStart().replace(":", "");

                                    Ticket ticket = new Ticket(ticketName, patient);

                                    appointment.setTicket(ticket);

                                    patientDao.addPersonToAppointment(appointment, patient);

                                    result = new AddPatientToAppointmentDtoResponse(appointment.getTicket().getName(),
                                            doctor.getId(), doctor.getFirstName(),
                                            doctor.getLastName(), doctor.getPatronymic(),
                                            doctor.getSpeciality(), doctor.getRoom(),
                                            dateOfAppointment.toString(), appointment.getTimeStart());
                                }
                            }
                        }
                    }
                }
            }
        }
        if (result == null) {
            throw new HospitalException(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_APPOINTMENT);
        }
        return result;
    }
}
