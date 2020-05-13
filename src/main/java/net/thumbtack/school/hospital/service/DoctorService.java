package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.dao.PatientDao;
import net.thumbtack.school.hospital.dto.internal.AppointmentForDto;
import net.thumbtack.school.hospital.dto.internal.DayScheduleForDto;
import net.thumbtack.school.hospital.dto.internal.PatientInfo;
import net.thumbtack.school.hospital.dto.internal.WeekSchedule;
import net.thumbtack.school.hospital.dto.request.AddPatientToCommissionDtoRequest;
import net.thumbtack.school.hospital.dto.request.DeleteDoctorScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.response.AddPatientToCommissionDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.model.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import org.modelmapper.ModelMapper;
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

    private DoctorDao doctorDao;
    private PatientDao patientDao;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public DoctorService(DoctorDao doctorDao, PatientDao patientDao) {
        this.doctorDao = doctorDao;
        this.patientDao = patientDao;
    }

    public ReturnDoctorDtoResponse registerDoctor(RegisterDoctorDtoRequest registerDoctorDtoRequest) {

        List<DaySchedule> schedule = createSchedule(registerDoctorDtoRequest.getWeekSchedule(),
                registerDoctorDtoRequest.getWeekDaysSchedule(), registerDoctorDtoRequest.getDateStart(),
                registerDoctorDtoRequest.getDateEnd(), registerDoctorDtoRequest.getDuration());
        Doctor doctor = modelMapper.map(registerDoctorDtoRequest, Doctor.class);
        doctor.setSchedule(schedule);
        doctor.setUserType(UserType.DOCTOR);

        doctor = doctorDao.insert(doctor);
        doctor.getSchedule()
                .stream()
                .sorted(Comparator.comparing(DaySchedule::getDate))
                .forEach(daySchedule -> daySchedule.getAppointmentList()
                        .sort(Comparator.comparing(Appointment::getTimeStart)));

        Map<String, List<AppointmentForDto>> scheduleForResponse = createScheduleForResponse(doctor.getSchedule());
        ReturnDoctorDtoResponse result = modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
        result.setSchedule(scheduleForResponse);

        return result;
    }

    public ReturnDoctorDtoResponse updateSchedule(UpdateScheduleDtoRequest updateScheduleDtoRequest, int id)
            throws HospitalException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate = LocalDate.parse(updateScheduleDtoRequest.getDateStart(), formatter);
        LocalDate endDate = LocalDate.parse(updateScheduleDtoRequest.getDateEnd(), formatter);

        List<DaySchedule> newSchedule = createSchedule(updateScheduleDtoRequest.getWeekSchedule(),
                updateScheduleDtoRequest.getWeekDaysSchedule(), updateScheduleDtoRequest.getDateStart(),
                updateScheduleDtoRequest.getDateEnd(), updateScheduleDtoRequest.getDuration());
        Doctor doctor = doctorDao.getById(id);
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

            boolean hasFreeDate = datesToChange.stream().anyMatch(dayScheduleFromDb -> dayScheduleFromDb.getAppointmentList()
                    .stream().allMatch(appointment -> appointment.getState().equals(AppointmentState.FREE)));
            if (hasFreeDate) {
                int doctorId = doctor.getId();
                datesToChange.forEach(dayScheduleFromDb -> {
                    boolean isFree = dayScheduleFromDb.getAppointmentList().stream().
                            allMatch(appointment -> appointment.getState().equals(AppointmentState.FREE));
                    if (isFree) {
                        Optional<DaySchedule> optionalNewDaySchedule = newSchedule.stream().
                                filter(daySchedule -> daySchedule.getDate().
                                        equals(dayScheduleFromDb.getDate())).findFirst();
                        if (optionalNewDaySchedule.isPresent()) {
                            DaySchedule newDaySchedule = doctorDao.updateDaySchedule(
                                    doctorId, dayScheduleFromDb, optionalNewDaySchedule.get());

                            scheduleFromDb.remove(dayScheduleFromDb);
                            scheduleFromDb.add(newDaySchedule);
                        }
                    }
                });
                doctor.setSchedule(scheduleFromDb);
            } else {
                throw new HospitalException(HospitalErrorCode.CAN_NOT_UPDATE_SCHEDULE);
            }
        }
        Map<String, List<AppointmentForDto>> scheduleForResponse = createScheduleForResponse(doctor.getSchedule());
        ReturnDoctorDtoResponse result = modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
        result.setSchedule(scheduleForResponse);
        return result;
    }

    public ReturnDoctorDtoResponse getDoctor(int doctorId) {
        Doctor doctor = doctorDao.getById(doctorId);

        doctor.getSchedule()
                .stream()
                .sorted(Comparator.comparing(DaySchedule::getDate))
                .forEach(daySchedule -> daySchedule.getAppointmentList()
                        .sort(Comparator.comparing(Appointment::getTimeStart)));

        Map<String, List<AppointmentForDto>> scheduleForResponse = createScheduleForResponse(doctor.getSchedule());
        ReturnDoctorDtoResponse result = modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
        result.setSchedule(scheduleForResponse);

        return result;
    }

    public List<ReturnDoctorDtoResponse> getAllDoctors(String speciality) {
        List<Doctor> doctorList;
        if (speciality.equals("null")) {
            doctorList = doctorDao.getAll();
        } else {
            doctorList = doctorDao.getAllBySpeciality(speciality);
        }
        List<ReturnDoctorDtoResponse> resultList = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            ReturnDoctorDtoResponse result = modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
            result.setSchedule(new HashMap<>());
            resultList.add(result);
        }
        return resultList;
    }

    public void deleteDoctorScheduleSinceDate(DeleteDoctorScheduleDtoRequest dtoRequest, int id) throws HospitalException {
        Doctor doctor = doctorDao.getById(id);
        if (doctor == null) {
            throw new HospitalException(HospitalErrorCode.WRONG_ID);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate lastDateOfWork = LocalDate.parse(dtoRequest.getDate(), formatter);
        doctorDao.deleteScheduleSinceDate(id, lastDateOfWork);
    }

    public AddPatientToCommissionDtoResponse addPatientToCommission(
            AddPatientToCommissionDtoRequest dtoRequest, int id) throws HospitalException {
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

        changeAllAppointmentsState(dateOfCommission, startOfCommission, endOfCommission, doctorList, commission);
        doctorDao.insertCommission(commission);

        return new AddPatientToCommissionDtoResponse(ticketNumber,
                patient.getId(), dtoRequest.getDoctorIds(), dtoRequest.getRoom(), dtoRequest.getDate(),
                dtoRequest.getDate(), dtoRequest.getDuration());
    }

    private void changeAllAppointmentsState(
            LocalDate dateOfCommission, LocalTime startOfCommission,
            LocalTime endOfCommission, List<Doctor> doctorList, Commission commission) throws HospitalException {

        List<Appointment> appointments = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            doctor.getCommissionList().add(commission);
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
                        throw new HospitalException(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_COMMISSION);
                    }
                    appointment.setState(AppointmentState.COMMISSION);
                    appointments.add(appointment);
                }
            }
        }
        doctorDao.changeAllAppointmentsState(appointments);
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
                throw new HospitalException(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_COMMISSION);
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Map<String, List<AppointmentForDto>> fullMap = new HashMap<>();
        for (DaySchedule daySchedule : schedule) {

            List<AppointmentForDto> appList = new ArrayList<>();
            for (Appointment appointment : daySchedule.getAppointmentList()) {

                if (appointment.getTicket() == null) {
                    appList.add(new AppointmentForDto(appointment.getTimeStart().toString()));
                } else {
                    Patient patient = patientDao.getById(appointment.getTicket().getId());
                    PatientInfo patientInfo = new PatientInfo(patient.getId(), patient.getFirstName(),
                            patient.getLastName(), patient.getPatronymic(), patient.getEmail(), patient.getAddress(),
                            patient.getPhone());
                    appList.add(new AppointmentForDto(appointment.getTimeStart().toString(), patientInfo));
                }
            }
            fullMap.put(daySchedule.getDate().format(formatter), appList);
        }
        return fullMap;
    }
}
