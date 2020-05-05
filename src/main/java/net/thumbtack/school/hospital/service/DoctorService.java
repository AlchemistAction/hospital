package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.internal.WeekSchedule;
import net.thumbtack.school.hospital.dto.request.AddPatientToCommissionDtoRequest;
import net.thumbtack.school.hospital.dto.request.ChangeScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.request.DeleteDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.response.AddPatientToCommissionDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.internal.DayScheduleForDto;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.model.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.mybatis.dao.DoctorDao;
import net.thumbtack.school.hospital.mybatis.dao.PatientDao;
import org.modelmapper.ModelMapper;

import javax.print.Doc;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class DoctorService {

    private DoctorDao doctorDao;
    private PatientDao patientDao;
    private ModelMapper modelMapper = new ModelMapper();

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

        doctor = doctorDao.insert(doctor);

        Map<String, Map<String, Patient>> scheduleForResponse = createScheduleForResponse(doctor.getSchedule());

        ReturnDoctorDtoResponse result = modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
        result.setSchedule(scheduleForResponse);

        return result;
    }

    public ReturnDoctorDtoResponse updateSchedule(ChangeScheduleDtoRequest changeScheduleDtoRequest, int id)
            throws HospitalException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate startDate = LocalDate.parse(changeScheduleDtoRequest.getDateStart(), formatter);
        LocalDate endDate = LocalDate.parse(changeScheduleDtoRequest.getDateEnd(), formatter);

        List<DaySchedule> newSchedule = createSchedule(changeScheduleDtoRequest.getWeekSchedule(),
                changeScheduleDtoRequest.getWeekDaysSchedule(), changeScheduleDtoRequest.getDateStart(),
                changeScheduleDtoRequest.getDateEnd(), changeScheduleDtoRequest.getDuration());

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
                        }
                );
                doctor.setSchedule(scheduleFromDb);
            } else {
                throw new HospitalException(HospitalErrorCode.CAN_NOT_UPDATE_SCHEDULE);
            }
        }

        Map<String, Map<String, Patient>> scheduleForResponse = createScheduleForResponse(doctor.getSchedule());

        ReturnDoctorDtoResponse result = modelMapper.map(doctor, ReturnDoctorDtoResponse.class);
        result.setSchedule(scheduleForResponse);

        return result;
    }

    public void deleteDoctor(DeleteDoctorDtoRequest deleteDoctorDtoRequest, int id) {
        Doctor doctor = doctorDao.getById(id);
        doctorDao.delete(doctor);
    }

    public List<Doctor> addPatientToCommission(
            AddPatientToCommissionDtoRequest dtoRequest, int id) throws HospitalException {

        Set<Integer> doctorIds = new HashSet<>(Arrays.asList(dtoRequest.getDoctorIds()));
        doctorIds.add(id);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateOfCommission = LocalDate.parse(dtoRequest.getDate(), formatter);

        LocalTime startOfCommission = LocalTime.parse(dtoRequest.getTime());
        LocalTime endOfCommission = startOfCommission.plusMinutes(LocalTime.parse(dtoRequest.getDuration()).getMinute());


        Patient patient = patientDao.getById(dtoRequest.getPatientId());

        List<Ticket> ticketList = patientDao.getAllTickets(patient);

        if (!(ticketList == null)) {

            for (Ticket ticket : ticketList) {
                if (!(ticket.getAppointment() == null)) {


                    if (ticket.getAppointment().getDaySchedule().getDate().equals(dateOfCommission)) {
                        if (ticket.getAppointment().getTimeEnd().isAfter(startOfCommission) &&
                                ticket.getAppointment().getTimeStart().isBefore(endOfCommission)) {
                            throw new HospitalException(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_COMMISSION);
                        }
                    }
                } else {
                    if (ticket.getCommission().getAppointmentList().get(0).getDaySchedule().getDate().equals(dateOfCommission)) {
                        if (ticket.getCommission().getAppointmentList().get(0).getTimeEnd().isAfter(startOfCommission) &&
                                ticket.getCommission().getAppointmentList().get(0).getTimeStart().isBefore(endOfCommission)) {
                            throw new HospitalException(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_COMMISSION);
                        }
                    }
                }
            }
        }


        Appointment appForCommission = new Appointment(startOfCommission, endOfCommission, AppointmentState.COMMISSION);

        List<Appointment> appointmentListFromDb = new ArrayList<>();

        List<Doctor> doctorList = new ArrayList<>();

        LocalTime startOfNewAppointment;
        LocalTime endOfOfNewAppointment;
        Appointment newAppointment;

        for (int doctorId : doctorIds) {
            Doctor doctor = doctorDao.getById(doctorId);
            doctorList.add(doctor);
        }

        for (Doctor doctor : doctorList) {
            for (DaySchedule daySchedule : doctor.getSchedule()) {
                if (daySchedule.getDate().equals(dateOfCommission)) {
                    for (Appointment appointment : daySchedule.getAppointmentList()) {
                        if (appointment.getTimeEnd().isAfter(startOfCommission) &&
                                appointment.getTimeStart().isBefore(endOfCommission)) {
                            if (!appointment.getState().equals(AppointmentState.FREE)) {
                                throw new HospitalException(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_COMMISSION);
                            }
                        }
                    }
                }
            }
        }


        for (Doctor doctor : doctorList) {

            Optional<DaySchedule> daySchedule = doctor.getSchedule().stream().
                    filter(d -> d.getDate().equals(dateOfCommission)).findFirst();

            if (daySchedule.isPresent()) {

                appForCommission.setDaySchedule(daySchedule.get());

                List<Appointment> appointmentList = daySchedule.get().getAppointmentList().stream().
                        filter(a -> (a.getTimeEnd().isAfter(startOfCommission) &&
                                a.getTimeStart().isBefore(endOfCommission))).collect(Collectors.toList());

                for (Appointment appointment : appointmentList) {

                    doctorDao.deleteAppointment(appointment);
                    daySchedule.get().getAppointmentList().remove(appointment);

                    if (appointment.getTimeStart().isAfter(startOfCommission) &&
                            appointment.getTimeEnd().isBefore(endOfCommission)) {

                        break;

                    } else if (appointment.getTimeStart().isBefore(startOfCommission)) {

                        startOfNewAppointment = appointment.getTimeStart();
                        endOfOfNewAppointment = startOfCommission;

                        newAppointment = new Appointment(startOfNewAppointment, endOfOfNewAppointment,
                                AppointmentState.FREE, daySchedule.get());

                        doctorDao.insertAppointment(newAppointment);
                        daySchedule.get().getAppointmentList().add(newAppointment);
                    } else {

                        startOfNewAppointment = endOfCommission;
                        endOfOfNewAppointment = appointment.getTimeEnd();

                        newAppointment = new Appointment(startOfNewAppointment, endOfOfNewAppointment,
                                AppointmentState.FREE, daySchedule.get());

                        doctorDao.insertAppointment(newAppointment);

                        daySchedule.get().getAppointmentList().add(newAppointment);
                    }
                }
                doctorDao.insertAppointment(appForCommission);

                daySchedule.get().getAppointmentList().add(appForCommission);

                appointmentListFromDb.add(appForCommission);
            } else {
                throw new HospitalException(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_COMMISSION);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (
                int doctorId : doctorIds) {
            sb.append(doctorId);
        }

        String ticketName = "CD" + sb.toString() +
                dateOfCommission.toString().replace("-", "") +
                startOfCommission.toString().replace(":", "");


        Commission commission = new Commission(appointmentListFromDb, dtoRequest.getRoom(), new Ticket(ticketName, patient));

        doctorDao.insertCommission(commission);

        AddPatientToCommissionDtoResponse dtoResponse = new AddPatientToCommissionDtoResponse(ticketName,
                patient.getId(), dtoRequest.getDoctorIds(), dtoRequest.getRoom(), dtoRequest.getDate(),
                dtoRequest.getDate(), dtoRequest.getDuration());

        return doctorList;
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
            return schedule;

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
            return schedule;
        }
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

    private Map<String, Map<String, Patient>> createScheduleForResponse(List<DaySchedule> schedule) {

        schedule.sort(Comparator.comparing(DaySchedule::getDate));
        schedule.forEach(daySchedule -> daySchedule.getAppointmentList().
                sort(Comparator.comparing(Appointment::getTimeStart)));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Map<String, Map<String, Patient>> fullMap = new TreeMap<>();

        for (DaySchedule daySchedule : schedule) {

            Map<String, Patient> timeMap = new TreeMap<>();

            for (Appointment appointment : daySchedule.getAppointmentList()) {

                if (appointment.getTicket() == null) {

                    timeMap.put(appointment.getTimeStart().toString(), null);

                } else {

                    Patient patient = patientDao.getById(appointment.getTicket().getId());
                    timeMap.put(appointment.getTimeStart().toString(), patient);
                }
            }

            fullMap.put(daySchedule.getDate().format(formatter), timeMap);
        }
        return fullMap;
    }
}
