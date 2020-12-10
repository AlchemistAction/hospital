package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.dao.PatientDao;
import net.thumbtack.school.hospital.dto.internal.AppointmentForDto;
import net.thumbtack.school.hospital.dto.internal.DayScheduleForDto;
import net.thumbtack.school.hospital.dto.internal.WeekSchedule;
import net.thumbtack.school.hospital.dto.request.AddPatientToCommissionDtoRequest;
import net.thumbtack.school.hospital.dto.request.DeleteDoctorScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateScheduleDtoRequest;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorDao doctorDao;
    private final PatientDao patientDao;
    private final ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorService.class);

    @Autowired
    public DoctorService(DoctorDao doctorDao, PatientDao patientDao) {
        this.doctorDao = doctorDao;
        this.patientDao = patientDao;
    }

    public ReturnDoctorDtoResponse registerDoctor(RegisterDoctorDtoRequest registerDoctorDtoRequest) throws HospitalException {
        LOGGER.info("Doctor Service register new Doctor {} ", registerDoctorDtoRequest);
        List<DaySchedule> schedule = createSchedule(registerDoctorDtoRequest.getWeekSchedule(),
                registerDoctorDtoRequest.getWeekDaysSchedule(), registerDoctorDtoRequest.getDateStart(),
                registerDoctorDtoRequest.getDateEnd(), registerDoctorDtoRequest.getDuration());
        Doctor doctor = modelMapper.map(registerDoctorDtoRequest, Doctor.class);
        doctor.setSchedule(schedule);
        doctor.setUserType(UserType.DOCTOR);
        try {
            doctor = doctorDao.insert(doctor);
        } catch (RuntimeException ex) {
            String massage = ex.getMessage();
            if (massage.contains("room")) {
                LOGGER.debug("Doctor Service cant register new Doctor, wrong room {}, {}",
                        registerDoctorDtoRequest.getRoom(), ex);
                throw new HospitalException(new ErrorModel(HospitalErrorCode.DUPLICATE_ROOM, "room",
                        "Room: " + "'" + registerDoctorDtoRequest.getRoom() + "'" + " is already occupied"));
            } else
                LOGGER.debug("Doctor Service cant register new Doctor, duplicate login {}, {}",
                        registerDoctorDtoRequest.getLogin(), ex);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.DUPLICATE_USER, "login",
                    "User with login: " + "'" + registerDoctorDtoRequest.getLogin() + "'" + " already exist"));
        }
        Map<String, List<AppointmentForDto>> scheduleForResponse = createScheduleForResponse(doctor.getSchedule());
        ReturnDoctorDtoResponse result = modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
        result.setSchedule(scheduleForResponse);

        return result;
    }

    public ReturnDoctorDtoResponse updateSchedule(UpdateScheduleDtoRequest updateScheduleDtoRequest, int id)
            throws HospitalException {
        LOGGER.info("Doctor Service update schedule {}, {}", updateScheduleDtoRequest, id);
        Doctor doctor = doctorDao.getById(id);
        if (doctor == null) {
            LOGGER.debug("Doctor Service cant update schedule, wrong ID {}, {}", updateScheduleDtoRequest, id);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_ID, "id",
                    "There is no doctor with ID: " + id));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate = LocalDate.parse(updateScheduleDtoRequest.getDateStart(), formatter);
        LocalDate endDate = LocalDate.parse(updateScheduleDtoRequest.getDateEnd(), formatter);

        List<DaySchedule> newSchedule = createSchedule(updateScheduleDtoRequest.getWeekSchedule(),
                updateScheduleDtoRequest.getWeekDaysSchedule(), updateScheduleDtoRequest.getDateStart(),
                updateScheduleDtoRequest.getDateEnd(), updateScheduleDtoRequest.getDuration());

        List<DaySchedule> scheduleFromDb = doctor.getSchedule();

        boolean isOutOfReach = endDate.isBefore(scheduleFromDb.get(0).getDate()) ||
                startDate.isAfter(scheduleFromDb.get(doctor.getSchedule().size() - 1).getDate());
        if (isOutOfReach) {
            List<DaySchedule> newScheduleFromDb = doctorDao.insertSchedule(newSchedule, doctor);
            doctor.getSchedule().addAll(newScheduleFromDb);
        } else {
            List<DaySchedule> datesToChange = scheduleFromDb.stream().
                    filter(dayScheduleFromDb -> dayScheduleFromDb.getDate().isAfter(startDate) &&
                            dayScheduleFromDb.getDate().isBefore(endDate)).collect(Collectors.toList());

            boolean hasFreeDate = datesToChange.stream()
                    .allMatch(dayScheduleFromDb -> dayScheduleFromDb.getAppointmentList()
                            .stream()
                            .allMatch(appointment -> appointment.getState().equals(AppointmentState.FREE)));
            if (hasFreeDate) {
                int doctorId = doctor.getId();
                datesToChange.forEach(dayScheduleFromDb -> {
                    Optional<DaySchedule> optionalNewDaySchedule = newSchedule.stream().
                            filter(daySchedule -> daySchedule.getDate().
                                    equals(dayScheduleFromDb.getDate())).findFirst();
                    if (optionalNewDaySchedule.isPresent()) {
                        DaySchedule newDaySchedule = doctorDao.updateDaySchedule(
                                doctorId, dayScheduleFromDb, optionalNewDaySchedule.get());

                        scheduleFromDb.remove(dayScheduleFromDb);
                        scheduleFromDb.add(newDaySchedule);
                    }
                });
                doctor.setSchedule(scheduleFromDb);
            } else {
                LOGGER.debug("Doctor Service cant update schedule, no free date {}, {}", updateScheduleDtoRequest, id);
                throw new HospitalException(new ErrorModel(HospitalErrorCode.CAN_NOT_UPDATE_SCHEDULE,
                        "schedule", "All dates are already occupied"));
            }
        }
        Map<String, List<AppointmentForDto>> scheduleForResponse = createScheduleForResponse(doctor.getSchedule());
        ReturnDoctorDtoResponse result = modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
        result.setSchedule(scheduleForResponse);
        return result;
    }

    public ReturnDoctorDtoResponse getDoctor(int doctorId, String schedule, String startDate, String endDate,
                                             int id, UserType userType) throws HospitalException {
        LOGGER.info("Doctor Service get Doctor {}, {}, {}, {}, {}, {}",
                doctorId, schedule, startDate, endDate, id, userType);
        Doctor doctor = doctorDao.getById(doctorId);
        if (doctor == null) {
            LOGGER.debug("Doctor Service cant get Doctor, wrong ID {}", doctorId);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_ID, "id",
                    "There is no doctor with ID: " + doctorId));
        }
        if (schedule.equals("no")) {
            ReturnDoctorDtoResponse result = modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
            result.setSchedule(new HashMap<>());
            return result;
        }
        LocalDate start = getStartDateForResponse(startDate);
        LocalDate end = getEndDateForResponse(endDate);

        List<DaySchedule> newSchedule = getScheduleWithinDatePeriod(doctor.getSchedule(), start, end);
        doctor.setSchedule(newSchedule);

        if (userType.equals(UserType.PATIENT)) {
            deleteOtherPatientInfo(id, doctor);
        }
        Map<String, List<AppointmentForDto>> scheduleForResponse = createScheduleForResponse(doctor.getSchedule());
        ReturnDoctorDtoResponse result = modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
        result.setSchedule(scheduleForResponse);
        return result;
    }

    public GetAllDoctorsDtoResponse getAllDoctors(String speciality, String schedule, String startDate,
                                                  String endDate, int id, UserType userType) {
        LOGGER.info("Doctor Service get all Doctors {}, {}, {}, {}, {}, {}",
                speciality, schedule, startDate, endDate, id, userType);
        List<Doctor> doctorList;
        if (speciality.equals("no")) {
            doctorList = doctorDao.getAll();
        } else {
            doctorList = doctorDao.getAllBySpeciality(speciality);
        }
        List<ReturnDoctorDtoResponse> resultList = new ArrayList<>();
        if (schedule.equals("no")) {
            for (Doctor doctor : doctorList) {
                ReturnDoctorDtoResponse result = modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
                result.setSchedule(new HashMap<>());
                resultList.add(result);
            }
            return new GetAllDoctorsDtoResponse(resultList);
        }
        LocalDate start = getStartDateForResponse(startDate);
        LocalDate end = getEndDateForResponse(endDate);

        for (Doctor doctor : doctorList) {
            List<DaySchedule> newSchedule = getScheduleWithinDatePeriod(doctor.getSchedule(), start, end);
            doctor.setSchedule(newSchedule);
            if (userType.equals(UserType.PATIENT)) {
                deleteOtherPatientInfo(id, doctor);
            }
            Map<String, List<AppointmentForDto>> scheduleForResponse = createScheduleForResponse(doctor.getSchedule());
            ReturnDoctorDtoResponse result = modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
            result.setSchedule(scheduleForResponse);
            resultList.add(result);
        }
        return new GetAllDoctorsDtoResponse(resultList);
    }

    public void deleteDoctorScheduleSinceDate(DeleteDoctorScheduleDtoRequest dtoRequest, int id) throws HospitalException {
        LOGGER.info("Doctor Service delete Doctor schedule since date {}, {}", dtoRequest, id);
        Doctor doctor = doctorDao.getById(id);
        if (doctor == null) {
            LOGGER.debug("Doctor Service cant delete Doctor schedule since date, wrong ID {}", id);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_ID, "id",
                    "There is no doctor with ID: " + id));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate lastDateOfWork = LocalDate.parse(dtoRequest.getDate(), formatter);

        massagePatientsAboutCancelledAppointment(doctor);
        doctorDao.deleteScheduleSinceDate(id, lastDateOfWork);
    }


    public AddPatientToCommissionDtoResponse addPatientToCommission(
            AddPatientToCommissionDtoRequest dtoRequest, int id) throws HospitalException {
        LOGGER.info("Doctor Service add Patient To Commission {}, {}", dtoRequest, id);
        Set<Integer> doctorIds = new HashSet<>(Arrays.asList(dtoRequest.getDoctorIds()));
        doctorIds.add(id);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateOfCommission = LocalDate.parse(dtoRequest.getDate(), formatter);
        LocalTime startOfCommission = LocalTime.parse(dtoRequest.getTime());
        LocalTime endOfCommission = startOfCommission.plusMinutes(LocalTime.parse(dtoRequest.getDuration()).getMinute());

        Patient patient = patientDao.getById(dtoRequest.getPatientId());
        List<Ticket> ticketList = patientDao.getAllTickets(patient);
        checkPatientTickets(dateOfCommission, startOfCommission, endOfCommission, ticketList);

        List<Doctor> doctorList = new ArrayList<>();
        for (int doctorId : doctorIds) {
            Doctor doctor = doctorDao.getById(doctorId);
            doctorList.add(doctor);
        }
        String ticketNumber = createTicketNumber(doctorIds, dateOfCommission, startOfCommission);
        Commission commission = new Commission(dateOfCommission, startOfCommission, endOfCommission,
                dtoRequest.getRoom(), doctorList, new Ticket(ticketNumber, patient));

        changeAllAppointmentsState(dateOfCommission, startOfCommission, endOfCommission, doctorList);
        doctorDao.insertCommission(commission);

        return new AddPatientToCommissionDtoResponse(ticketNumber,
                patient.getId(), dtoRequest.getDoctorIds(), dtoRequest.getRoom(), dtoRequest.getDate(),
                dtoRequest.getDate(), dtoRequest.getDuration());
    }

    public UserStatisticsDtoResponse getDoctorStatistics(int doctorId, String startDate, String endDate) throws HospitalException {
        LOGGER.info("Doctor Service getDoctorStatistics {}, {}, {}", doctorId, startDate, endDate);
        Doctor doctor = doctorDao.getById(doctorId);
        if (doctor == null) {
            LOGGER.debug("Doctor Service cant getDoctorStatistics, wrong ID {}", doctorId);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_ID, "id",
                    "There is no doctor with ID: " + doctorId));
        }
        LocalDate start = getStartDateForResponse(startDate);
        LocalDate end = getEndDateForResponse(endDate);
        List<String> resultList = createStatisticsList(doctor, start, end);
        return new UserStatisticsDtoResponse(resultList);
    }

    public GetAllDoctorsStatistics getAllDoctorsStatistics(String speciality, String startDate, String endDate) {
        LOGGER.info("Doctor Service getAllDoctorsStatistics {}, {}, {}", speciality, startDate, endDate);
        List<Doctor> doctorList;
        if (speciality.equals("no")) {
            doctorList = doctorDao.getAll();
        } else {
            doctorList = doctorDao.getAllBySpeciality(speciality);
        }
        LocalDate start = getStartDateForResponse(startDate);
        LocalDate end = getEndDateForResponse(endDate);

        List<UserStatisticsDtoResponse> resultList = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            List<String> list = createStatisticsList(doctor, start, end);
            resultList.add(new UserStatisticsDtoResponse(list));
        }
        return new GetAllDoctorsStatistics(resultList);
    }

    private List<String> createStatisticsList(Doctor doctor, LocalDate start, LocalDate end) {
        int numberOfAppointments = (int) doctor.getSchedule().stream()
                .filter(daySchedule -> daySchedule.getDate().isBefore(end.plusDays(1))
                        && daySchedule.getDate().isAfter(start.minusDays(1)))
                .flatMap(daySchedule -> daySchedule.getAppointmentList().stream())
                .filter(appointment -> appointment.getTicket() != null).count();
        int numberOfCommissions = (int) doctor.getCommissionList().stream()
                .filter(commission -> commission.getDate().isBefore(end.plusDays(1))
                        && commission.getDate().isAfter(start.minusDays(1))).count();

        List<String> resultList = new ArrayList<>();
        resultList.add("Doctor ID: " + doctor.getId());
        resultList.add(numberOfAppointments + " Appointment(s)");
        resultList.add(numberOfCommissions + " Commission(s)");
        return resultList;
    }

    private LocalDate getStartDateForResponse(String startDate) {
        if (startDate.equals("no")) {
            return LocalDate.of(2020, 4, 2);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(startDate, formatter);
    }

    private LocalDate getEndDateForResponse(String endDate) {
        if (endDate.equals("no")) {
            final int threeMonths = 90;
            return LocalDate.of(2020, 4, 2).plusDays(threeMonths);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(endDate, formatter);
    }

    private List<DaySchedule> getScheduleWithinDatePeriod(List<DaySchedule> schedule, LocalDate start, LocalDate end) {
        return schedule.stream()
                .filter(daySchedule -> daySchedule.getDate().isAfter(start.minusDays(1)) &&
                        daySchedule.getDate().isBefore(end.plusDays(1)))
                .collect(Collectors.toList());
    }

    private void deleteOtherPatientInfo(int id, Doctor doctor) {
        doctor.getSchedule().stream()
                .flatMap(daySchedule -> daySchedule.getAppointmentList().stream())
                .forEach(appointment -> {
                    if (appointment.getTicket() != null) {
                        Patient patient = appointment.getTicket().getPatient();
                        if (patient.getId() != id) {
                            appointment.getTicket().setPatient(null);
                        }
                    }
                });
    }

    private void changeAllAppointmentsState(
            LocalDate dateOfCommission, LocalTime startOfCommission,
            LocalTime endOfCommission, List<Doctor> doctorList) throws HospitalException {

        List<Appointment> appointments = new ArrayList<>();

        for (Doctor doctor : doctorList) {
            Optional<DaySchedule> daySchedule = doctor.getSchedule()
                    .stream()
                    .filter(d -> d.getDate().equals(dateOfCommission))
                    .findFirst();

            if (daySchedule.isPresent()) {
                List<Appointment> appointmentList = daySchedule.get().getAppointmentList()
                        .stream()
                        .filter(a -> (a.getTimeEnd().isAfter(startOfCommission) &&
                                a.getTimeStart().isBefore(endOfCommission)))
                        .collect(Collectors.toList());

                for (Appointment appointment : appointmentList) {
                    if (!appointment.getState().equals(AppointmentState.FREE)) {
                        LOGGER.debug("Doctor Service cant add Patient To Commission. Doctor with ID {} has no free time",
                                doctor.getId());
                        throw new HospitalException(new ErrorModel(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_COMMISSION,
                                "doctorIds", "Doctor with ID: " + doctor.getId() +
                                " has no free time for commission"));
                    }
                    appointment.setState(AppointmentState.COMMISSION);
                    appointments.add(appointment);
                }
            }
        }
        doctorDao.changeAllAppointmentsStateToAppointmentOrCommission(appointments);
    }

    private String createTicketNumber(Set<Integer> doctorIds, LocalDate dateOfCommission, LocalTime startOfCommission) {
        StringBuilder sb = new StringBuilder();
        for (int doctorId : doctorIds) {
            sb.append(doctorId);
        }

        return "CD" + sb.toString() +
                dateOfCommission.toString().replace("-", "") +
                startOfCommission.toString().replace(":", "");
    }

    private void checkPatientTickets(LocalDate dateOfCommission, LocalTime startOfCommission,
                                     LocalTime endOfCommission, List<Ticket> ticketList) throws HospitalException {
        if (!(ticketList == null)) {
            for (Ticket ticket : ticketList) {
                if (!(ticket.getAppointment() == null)) {
                    checkAppointment(dateOfCommission, startOfCommission, endOfCommission,
                            ticket.getAppointment().getDaySchedule().getDate(), ticket.getAppointment().getTimeEnd(),
                            ticket.getAppointment().getTimeStart());
                } else {
                    checkAppointment(dateOfCommission, startOfCommission, endOfCommission,
                            ticket.getCommission().getDate(), ticket.getCommission().getTimeEnd(),
                            ticket.getCommission().getTimeStart());
                }
            }
        }
    }

    private void checkAppointment(LocalDate dateOfCommission, LocalTime startOfCommission, LocalTime endOfCommission,
                                  LocalDate date, LocalTime timeEnd, LocalTime timeStart) throws HospitalException {
        if (date.equals(dateOfCommission)) {
            if (timeEnd.isAfter(startOfCommission) &&
                    timeStart.isBefore(endOfCommission)) {
                LOGGER.debug("Doctor Service cant add Patient To Commission. Patient has no free time");
                throw new HospitalException(new ErrorModel(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_COMMISSION,
                        "patientId", "Patient has no free time for commission"));
            }
        }
    }

    private List<DaySchedule> createSchedule(WeekSchedule weekSchedule, DayScheduleForDto[] weekDaysSchedule,
                                             String startDate, String endDate, String durationFromDto) {

        List<LocalDate> dateListForAllPeriod = getDatesForAllPeriod(startDate, endDate);
        List<DaySchedule> schedule = new ArrayList<>();

        if (weekSchedule != null) {
            EnumSet<DayOfWeek> weekDays = Arrays.stream(weekSchedule.getWeekDays()).
                    map(s -> DayOfWeek.valueOf(s.toUpperCase())).
                    collect(Collectors.toCollection(() -> EnumSet.noneOf(DayOfWeek.class)));

            for (LocalDate date : dateListForAllPeriod) {
                if (weekDays.contains(date.getDayOfWeek())) {
                    List<Appointment> appointmentList = getAppointments(weekSchedule.getTimeStart(),
                            weekSchedule.getTimeEnd(), durationFromDto);
                    DaySchedule daySchedule = new DaySchedule(date, appointmentList);
                    schedule.add(daySchedule);
                }
            }
        } else {

            for (DayScheduleForDto dayScheduleForDto : weekDaysSchedule) {
                for (LocalDate date : dateListForAllPeriod) {
                    if (date.getDayOfWeek().name().toLowerCase().equals(dayScheduleForDto.getWeekDay().toLowerCase())) {

                        List<Appointment> appointmentList = getAppointments(dayScheduleForDto.getTimeStart(),
                                dayScheduleForDto.getTimeEnd(), durationFromDto);
                        DaySchedule daySchedule = new DaySchedule(date, appointmentList);
                        schedule.add(daySchedule);
                    }
                }
            }
        }
        return schedule;
    }

    private List<Appointment> getAppointments(String startOfWork, String endOfWork, String durationFromDto) {

        LocalTime start = LocalTime.parse(startOfWork);
        LocalTime end = LocalTime.parse(endOfWork);
        LocalTime duration = LocalTime.parse(durationFromDto);
        List<Appointment> appointmentList = new ArrayList<>();

        for (LocalTime time = start; time.isBefore(end); time = time.plusMinutes(duration.getMinute())) {
            Appointment appointment = new Appointment(time, time.plusMinutes(duration.getMinute()),
                    AppointmentState.FREE);
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    private List<LocalDate> getDatesForAllPeriod(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        DayOfWeek sat = DayOfWeek.of(6);
        DayOfWeek sund = DayOfWeek.of(7);
        EnumSet<DayOfWeek> weekEnd = EnumSet.of(sat, sund);
        List<LocalDate> dateList = new ArrayList<>();

        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            if (!weekEnd.contains(date.getDayOfWeek())) {
                dateList.add(date);
            }
        }
        return dateList;
    }

    private Map<String, List<AppointmentForDto>> createScheduleForResponse(List<DaySchedule> schedule) {

        schedule.sort(Comparator.comparing(DaySchedule::getDate));

        schedule.forEach(daySchedule -> daySchedule.getAppointmentList()
                .sort(Comparator.comparing(Appointment::getTimeStart)));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Map<String, List<AppointmentForDto>> fullMap = new LinkedHashMap<>();
        for (DaySchedule daySchedule : schedule) {

            List<AppointmentForDto> appList = new ArrayList<>();
            for (Appointment appointment : daySchedule.getAppointmentList()) {

                if (appointment.getTicket() == null) {
                    appList.add(new AppointmentForDto(appointment.getTimeStart().toString()));
                } else {
                    Patient patient = appointment.getTicket().getPatient();
                    if (patient == null) {
                        appList.add(new AppointmentForDto(appointment.getTimeStart().toString(),
                                "Already occupied"));
                    } else {
                        appList.add(new AppointmentForDto(appointment.getTimeStart().toString(), patient.getId(),
                                patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                                patient.getEmail(), patient.getAddress(), patient.getPhone()));
                    }
                }
            }
            fullMap.put(daySchedule.getDate().format(formatter), appList);
        }
        return fullMap;
    }

    private void massagePatientsAboutCancelledAppointment(Doctor doctor) {
        doctor.getSchedule().stream()
                .flatMap(daySchedule -> daySchedule.getAppointmentList().stream())
                .forEach(appointment -> {
                    if (appointment.getTicket() != null) {
                        Patient patient = appointment.getTicket().getPatient();
                        LOGGER.info("Massage about cancelled appointment was sent to patient email {} and phone {}",
                                patient.getEmail(), patient.getPhone());
                    }
                });
    }

}
