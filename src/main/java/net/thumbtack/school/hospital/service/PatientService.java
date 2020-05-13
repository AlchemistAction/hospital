package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.dao.PatientDao;
import net.thumbtack.school.hospital.dao.dao.TicketDao;
import net.thumbtack.school.hospital.dto.internal.DoctorInfo;
import net.thumbtack.school.hospital.dto.request.AddPatientToAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.AddPatientToAppointmentDtoResponse;
import net.thumbtack.school.hospital.dto.response.GetAllTicketsDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.model.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private PatientDao patientDao;
    private DoctorDao doctorDao;
    private TicketDao ticketDao;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public PatientService(PatientDao patientDao, DoctorDao doctorDao, TicketDao ticketDao) {
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
        this.ticketDao = ticketDao;
    }

    public ReturnPatientDtoResponse registerPatient(RegisterPatientDtoRequest registerPatientDtoRequest) {
        Patient patient = modelMapper.map(registerPatientDtoRequest, Patient.class);
        patient.setPhone(patient.getPhone().replace("-", ""));
        patient.setUserType(UserType.PATIENT);
        patient = patientDao.insert(patient);

        return modelMapper.map(patient, ReturnPatientDtoResponse.class);
    }

    public ReturnPatientDtoResponse getPatient(int patientId) {
        Patient patient = patientDao.getById(patientId);

        return modelMapper.map(patient, ReturnPatientDtoResponse.class);
    }

    public ReturnPatientDtoResponse updatePatient(
            UpdatePatientDtoRequest updatePatientDtoRequest, int id) throws HospitalException {

        Patient patient = patientDao.getById(id);

        if (!patient.getPassword().equals(updatePatientDtoRequest.getOldPassword())) {
            throw new HospitalException(HospitalErrorCode.WRONG_PASSWORD);
        }

        patient.setFirstName(updatePatientDtoRequest.getFirstName());
        patient.setLastName(updatePatientDtoRequest.getLastName());
        patient.setPassword(updatePatientDtoRequest.getPatronymic());
        patient.setEmail(updatePatientDtoRequest.getEmail());
        patient.setAddress(updatePatientDtoRequest.getAddress());
        patient.setPhone(updatePatientDtoRequest.getPhone());
        patient.setPassword(updatePatientDtoRequest.getNewPassword());

        patient = patientDao.update(patient);

        return modelMapper.map(patient, ReturnPatientDtoResponse.class);
    }

    public AddPatientToAppointmentDtoResponse addPatientToAppointment(
            AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest, int patientId) throws HospitalException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateOfAppointment = LocalDate.parse(addPatientToAppointmentDtoRequest.getDate(), formatter);

        Patient patient = patientDao.getById(patientId);

        AddPatientToAppointmentDtoResponse result = null;

        if (addPatientToAppointmentDtoRequest.getSpeciality() == null) {
            Doctor doctor = doctorDao.getById(addPatientToAppointmentDtoRequest.getDoctorId());

            result = findAppointment(addPatientToAppointmentDtoRequest.getTime(), dateOfAppointment, patient, doctor);

        } else {
            List<Doctor> doctorList = doctorDao.getAllBySpeciality(addPatientToAppointmentDtoRequest.getSpeciality());
            for (Doctor doctor : doctorList) {
                if (doctor.getSpeciality().equals(addPatientToAppointmentDtoRequest.getSpeciality())) {
                    result = findAppointment(addPatientToAppointmentDtoRequest.getTime(),
                            dateOfAppointment, patient, doctor);
                }
            }
        }
        if (result == null) {
            throw new HospitalException(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_APPOINTMENT);
        }
        return result;
    }

    private AddPatientToAppointmentDtoResponse findAppointment(String time, LocalDate dateOfAppointment,
                                                               Patient patient, Doctor doctor) throws HospitalException {

        AddPatientToAppointmentDtoResponse result = null;

        for (DaySchedule daySchedule : doctor.getSchedule()) {
            if (daySchedule.getDate().equals(dateOfAppointment)) {
                for (Appointment appointment : daySchedule.getAppointmentList()) {
                    if (appointment.getTimeStart().toString().equals(time)) {

                        String ticketName = "D" + doctor.getId() +
                                daySchedule.getDate().toString().replace("-", "") +
                                appointment.getTimeStart().toString().replace(":", "");
                        Ticket ticket = new Ticket(ticketName, patient);
                        appointment.setTicket(ticket);

                        patientDao.addPatientToAppointment(appointment, patient);
                        result = new AddPatientToAppointmentDtoResponse(appointment.getTicket().getNumber(),
                                doctor.getId(), doctor.getFirstName(),
                                doctor.getLastName(), doctor.getPatronymic(),
                                doctor.getSpeciality(), doctor.getRoom(),
                                dateOfAppointment.toString(), appointment.getTimeStart().toString());
                    }
                }
            }
        }
        return result;
    }

    public List<GetAllTicketsDtoResponse> getAllTickets(int id) {

        List<GetAllTicketsDtoResponse> responseList = new ArrayList<>();
        Patient patient = patientDao.getById(id);
        List<Ticket> ticketList = patientDao.getAllTickets(patient);
        if (!(ticketList == null)) {
            for (Ticket ticket : ticketList) {
                if (!(ticket.getAppointment() == null)) {
                    Doctor doctor = ticket.getAppointment().getDaySchedule().getDoctor();
                    String date = ticket.getAppointment().getDaySchedule().getDate().toString();
                    String time = ticket.getAppointment().getTimeStart().toString();

                    GetAllTicketsDtoResponse response = new GetAllTicketsDtoResponse(ticket.getNumber(), doctor.getRoom(),
                            date, time, doctor.getId(), doctor.getFirstName(), doctor.getLastName(),
                            doctor.getPatronymic(), doctor.getSpeciality());
                    responseList.add(response);
                } else {
                    List<Doctor> doctorList = ticket.getCommission().getDoctorList();
                    String room = ticket.getCommission().getRoom();
                    String date = ticket.getCommission().getDate().toString();
                    String time = ticket.getCommission().getTimeStart().toString();
                    List<DoctorInfo> list = new ArrayList<>();

                    for (Doctor doctor : doctorList) {
                        DoctorInfo doctorInfo = new DoctorInfo(doctor.getId(), doctor.getFirstName(),
                                doctor.getLastName(), doctor.getPatronymic(), doctor.getSpeciality());
                        list.add(doctorInfo);
                    }
                    GetAllTicketsDtoResponse response = new GetAllTicketsDtoResponse(ticket.getNumber(), room,
                            date, time, list);
                    responseList.add(response);
                }
            }
        }
        return responseList;
    }

    public void cancelAppointment(int id, UserType userType, String ticketNumber) throws HospitalException {
        Ticket ticket = ticketDao.getByNumber(ticketNumber);
        Doctor doctor = ticket.getAppointment().getDaySchedule().getDoctor();
        if (ticket.getPatient().getId() == id || doctor.getId() == id || userType.equals(UserType.ADMIN)) {
            ticketDao.delete(ticket);
        } else {
            throw new HospitalException(HospitalErrorCode.CAN_NOT_CANCEL_APPOINTMENT);
        }
    }

    public void cancelCommission(String ticketNumber) {

        Ticket ticket = ticketDao.getByNumber(ticketNumber);
        Commission commission = ticket.getCommission();

        commission.getDoctorList()
                .stream()
                .flatMap(doctor -> doctor.getSchedule().stream())
                .filter(daySchedule -> daySchedule.getDate().equals(commission.getDate()))
                .flatMap(daySchedule -> daySchedule.getAppointmentList().stream())
                .filter(a -> (a.getTimeEnd().isAfter(commission.getTimeStart()) &&
                        a.getTimeStart().isBefore(commission.getTimeEnd())))
                .forEach(appointment -> {
                    appointment.setState(AppointmentState.FREE);
                    doctorDao.changeAppointmentState(appointment);
                });

        ticketDao.delete(ticket);
    }
}