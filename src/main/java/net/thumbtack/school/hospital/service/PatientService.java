package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.dao.PatientDao;
import net.thumbtack.school.hospital.dao.dao.TicketDao;
import net.thumbtack.school.hospital.dto.internal.DoctorInfo;
import net.thumbtack.school.hospital.dto.request.AddPatientToAppointmentDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.validator.ErrorModel;
import net.thumbtack.school.hospital.validator.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientDao patientDao;
    private final DoctorDao doctorDao;
    private final TicketDao ticketDao;
    private final ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientService.class);

    @Autowired
    public PatientService(PatientDao patientDao, DoctorDao doctorDao, TicketDao ticketDao) {
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
        this.ticketDao = ticketDao;
    }

    public ReturnPatientDtoResponse registerPatient(RegisterPatientDtoRequest registerPatientDtoRequest) throws HospitalException {
        LOGGER.info("Patient Service register new Patient {} ", registerPatientDtoRequest);
        Patient patient = modelMapper.map(registerPatientDtoRequest, Patient.class);
        patient.setPhone(patient.getPhone().replace("-", ""));
        patient.setUserType(UserType.PATIENT);
        try {
            patient = patientDao.insert(patient);
        } catch (RuntimeException ex) {
            LOGGER.debug("Patient Service cant register new Patient, duplicate login, {}, {} ",
                    registerPatientDtoRequest.getLogin(), ex);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.DUPLICATE_USER, "login",
                    "User with login: " + "'" + registerPatientDtoRequest.getLogin() + "'" + " already exist"));
        }
        return modelMapper.map(patient, ReturnPatientDtoResponse.class);
    }

    public ReturnPatientDtoResponse getPatient(int patientId) throws HospitalException {
        LOGGER.info("Patient Service get Patient {} ", patientId);
        Patient patient = patientDao.getById(patientId);
        if (patient == null) {
            LOGGER.debug("Patient Service cant get Patient. wrong ID, {}", patientId);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_ID, "patientId",
                    "There is no Patient with ID: " + patientId));
        }
        return modelMapper.map(patient, ReturnPatientDtoResponse.class);
    }

    public ReturnPatientDtoResponse updatePatient(
            UpdatePatientDtoRequest updatePatientDtoRequest, int id) throws HospitalException {
        LOGGER.info("Patient Service update Patient {} ", id);
        Patient patient = patientDao.getById(id);
        if (!patient.getPassword().equals(updatePatientDtoRequest.getOldPassword())) {
            LOGGER.debug("Patient Service update Patient. wrong password {} ", id);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_PASSWORD,
                    "password", "Passwords don't match"));
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
            AddPatientToAppointmentDtoRequest dtoRequest, int patientId) throws HospitalException {
        LOGGER.info("Patient Service add Patient To Appointment {}, {}", dtoRequest, patientId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateOfAppointment = LocalDate.parse(dtoRequest.getDate(), formatter);

        if (dateOfAppointment.isAfter(LocalDate.now().plusDays(60))) {
            LOGGER.debug("Patient Service cant add Patient To Appointment, date {} too far", dtoRequest.getDate());
            throw new HospitalException(new ErrorModel(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_APPOINTMENT,
                    "date", "Doctor with ID: " + dtoRequest.getDoctorId() +
                    " has no Appointments on " + dtoRequest.getDate() + " date"));
        }

        Patient patient = patientDao.getById(patientId);
        AddPatientToAppointmentDtoResponse result = null;
        if (dtoRequest.getSpeciality() == null) {
            Doctor doctor = doctorDao.getById(dtoRequest.getDoctorId());

            result = findAppointment(dtoRequest.getTime(), dateOfAppointment, patient, doctor);
        } else {
            List<Doctor> doctorList = doctorDao.getAllBySpeciality(dtoRequest.getSpeciality());
            for (Doctor doctor : doctorList) {
                if (doctor.getSpeciality().equals(dtoRequest.getSpeciality())) {
                    result = findAppointment(dtoRequest.getTime(),
                            dateOfAppointment, patient, doctor);
                }
            }
        }
        if (result == null) {
            LOGGER.debug("Patient Service cant add Patient To Appointment. Doctor with Id {}" +
                    "has no appointments on date {}", dtoRequest.getDoctorId(), dtoRequest.getDate());
            throw new HospitalException(new ErrorModel(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_APPOINTMENT,
                    "date", "Doctor with ID: " + dtoRequest.getDoctorId() +
                    " has no Appointments on " + dtoRequest.getDate() + " date"));
        }
        LOGGER.info("Massage about appointment was sent to patient email {} and phone {}",
                patient.getEmail(), patient.getPhone());
        return result;
    }

    public GetAllTicketsDtoResponse getAllTickets(int id) {
        LOGGER.info("Patient Service get all tickets {}", id);
        List<GetTicketDtoResponse> responseList = new ArrayList<>();
        Patient patient = patientDao.getById(id);
        List<Ticket> ticketList = patientDao.getAllTickets(patient);
        if (!(ticketList == null)) {
            for (Ticket ticket : ticketList) {
                if (!(ticket.getAppointment() == null)) {
                    Doctor doctor = ticket.getAppointment().getDaySchedule().getDoctor();
                    String date = ticket.getAppointment().getDaySchedule().getDate().toString();
                    String time = ticket.getAppointment().getTimeStart().toString();

                    GetTicketDtoResponse response = new GetTicketDtoResponse(ticket.getNumber(), doctor.getRoom(),
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
                    GetTicketDtoResponse response = new GetTicketDtoResponse(ticket.getNumber(), room,
                            date, time, list);
                    responseList.add(response);
                }
            }
        }
        return new GetAllTicketsDtoResponse(responseList);
    }

    public void cancelAppointment(int id, UserType userType, String ticketNumber) throws HospitalException {
        LOGGER.info("Patient Service cancel appointment {}, {}, {}", id, userType, ticketNumber);
        Ticket ticket = ticketDao.getByNumber(ticketNumber);
        Doctor doctor = ticket.getAppointment().getDaySchedule().getDoctor();
        if (ticket.getPatient().getId() == id || doctor.getId() == id || userType.equals(UserType.ADMIN)) {
            ticketDao.delete(ticket);
            doctorDao.changeAppointmentStateToFree(ticket.getAppointment());
        } else {
            LOGGER.debug("Patient Service cant cancel appointment, wrong userType {}, {}, {}", id, userType, ticketNumber);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.CAN_NOT_CANCEL_APPOINTMENT,
                    "id", "You can not cancel other people appointment"));
        }
    }

    public void cancelCommission(String ticketNumber) {
        LOGGER.info("Patient Service cancel commission {}", ticketNumber);
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
                    doctorDao.changeAppointmentStateToFree(appointment);
                });
        ticketDao.delete(ticket);
    }

    public UserStatisticsDtoResponse getPatientStatistics(int patientId, String startDate, String endDate) throws HospitalException {
        LOGGER.info("Patient Service getStatistics {}, {}, {}", patientId, startDate, endDate);
        Patient patient = patientDao.getById(patientId);
        if (patient == null) {
            LOGGER.debug("Patient Service cant getStatistics. wrong patient ID, {}", patientId);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_ID, "patientId",
                    "There is no Patient with ID: " + patientId));
        }
        List<Ticket> ticketList = patientDao.getAllTickets(patient);
        LocalDate start = getStartDateForResponse(startDate);
        LocalDate end = getEndDateForResponse(endDate);
        List<Doctor> doctorList = doctorDao.getAll();

        Map<String, Integer> specialityMap = createSpecialityMap(doctorList);
        fillSpecialityMap(ticketList, start, end, specialityMap);

        List<String> resultList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : specialityMap.entrySet()) {
            resultList.add(entry.getValue() + " Appointment(s) to Doctors with speciality: " + entry.getKey());
        }
        int numberOfCommissions = (int) ticketList.stream().filter(ticket -> ticket.getCommission() != null).count();
        resultList.add(numberOfCommissions + " Commission(s)");
        return new UserStatisticsDtoResponse(resultList);
    }

    private Map<String, Integer> createSpecialityMap(List<Doctor> doctorList) {
        Set<String> specialitySet = new HashSet<>();
        doctorList.forEach(doctor -> specialitySet.add(doctor.getSpeciality()));
        Map<String, Integer> map = new HashMap<>();
        specialitySet.forEach(s -> map.put(s, 0));
        return map;
    }

    private void fillSpecialityMap(List<Ticket> ticketList, LocalDate start, LocalDate end, Map<String, Integer> specialityMap) {
        List<Appointment> appointments = ticketList.stream()
                .filter(ticket -> ticket.getAppointment() != null)
                .filter(ticket -> ticket.getAppointment().getDaySchedule().getDate().isBefore(end.plusDays(1))
                        && ticket.getAppointment().getDaySchedule().getDate().isAfter(start.minusDays(1)))
                .map(Ticket::getAppointment).collect(Collectors.toList());

        appointments.forEach(appointment -> {
            String speciality = appointment.getDaySchedule().getDoctor().getSpeciality();
            specialityMap.put(speciality, specialityMap.get(speciality) + 1);
        });
    }

    private AddPatientToAppointmentDtoResponse findAppointment(String time, LocalDate dateOfAppointment,
                                                               Patient patient, Doctor doctor) throws HospitalException {
        AddPatientToAppointmentDtoResponse result = null;
        checkPatientHasAnAppointmentOnDateWithSameDoctor(dateOfAppointment, patient, doctor);
        for (DaySchedule daySchedule : doctor.getSchedule()) {
            if (daySchedule.getDate().equals(dateOfAppointment)) {
                for (Appointment appointment : daySchedule.getAppointmentList()) {
                    if (appointment.getTimeStart().toString().equals(time)) {

                        String ticketName = "D" + doctor.getId() +
                                daySchedule.getDate().toString().replace("-", "") +
                                appointment.getTimeStart().toString().replace(":", "");
                        Ticket ticket = new Ticket(ticketName, patient);
                        appointment.setTicket(ticket);
                        appointment.setState(AppointmentState.APPOINTMENT);

                        patientDao.addPatientToAppointment(appointment, patient);
                        result = new AddPatientToAppointmentDtoResponse(appointment.getTicket().getNumber(),
                                doctor, dateOfAppointment.toString(), appointment.getTimeStart().toString());
                    }
                }
            }
        }
        return result;
    }

    private void checkPatientHasAnAppointmentOnDateWithSameDoctor(
            LocalDate dateOfAppointment, Patient patient, Doctor doctor) throws HospitalException {

        boolean patientHasAppointmentWithThatDoctor = doctor.getSchedule().stream()
                .filter(daySchedule -> daySchedule.getDate().equals(dateOfAppointment))
                .flatMap(daySchedule -> daySchedule.getAppointmentList().stream())
                .map(Appointment::getTicket)
                .anyMatch(ticket -> {
                    if (ticket != null) {
                        return ticket.getPatient().getId() == patient.getId();
                    }
                    return false;
                });
        if (patientHasAppointmentWithThatDoctor) {
            LOGGER.info("Patient Service cant add patient to appointment. Patient with ID {}" +
                            " already has appointment with doctor with ID {} on date {}",
                    patient.getId(), doctor.getId(), dateOfAppointment);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_APPOINTMENT,
                    "date",
                    "You have already made an appointment with doctor with ID: " + doctor.getId() +
                            " on day: " + dateOfAppointment + ". Please choose another day."));
        }
    }

    private LocalDate getStartDateForResponse(String startDate) {
        if (startDate.equals("no")) {
            return LocalDate.now();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(startDate, formatter);
    }

    private LocalDate getEndDateForResponse(String endDate) {
        if (endDate.equals("no")) {
            final int twoMonths = 60;
            return LocalDate.now().plusDays(twoMonths);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(endDate, formatter);
    }


}