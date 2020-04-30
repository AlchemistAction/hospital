package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.internal.WeekSchedule;
import net.thumbtack.school.hospital.dto.request.ChangeScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.internal.DayScheduleForDto;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.model.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.mybatis.dao.DoctorDao;
import net.thumbtack.school.hospital.mybatis.dao.PatientDao;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private List<DaySchedule> createSchedule(WeekSchedule weekSchedule, DayScheduleForDto[] weekDaysSchedule,
                                             String startDate, String endDate, String durationFromDto) {

        List<LocalDate> dateListForAllPeriod = getDatesForAllPeriod(startDate, endDate);

        List<DaySchedule> schedule = new ArrayList<>();

        if (weekSchedule != null) {

            for (String dateString : weekSchedule.getWeekDays()) {
                for (LocalDate date : dateListForAllPeriod) {
                    if (date.getDayOfWeek().name().toLowerCase().equals(dateString.toLowerCase())) {

                        List<Appointment> appointmentList = getAppointments(weekSchedule.getTimeStart(),
                                weekSchedule.getTimeEnd(), durationFromDto);

                        DaySchedule daySchedule = new DaySchedule(date, appointmentList);
                        schedule.add(daySchedule);
                    }
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

        List<Appointment> appointmentList = new ArrayList<>();

        for (LocalTime time = start; time.isBefore(end); time = time.plusMinutes(duration.getMinute())) {

            Appointment appointment = new Appointment(time.format(dtf),
                    time.plusMinutes(duration.getMinute()).format(dtf), AppointmentState.FREE);

            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    private List<LocalDate> getDatesForAllPeriod(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        List<LocalDate> dateList = new ArrayList<>();

        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            if (!date.getDayOfWeek().name().equals("Saturday") && !date.getDayOfWeek().name().equals("Sunday"))
                dateList.add(date);
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

                    timeMap.put(appointment.getTimeStart(), null);

                } else {

                    Patient patient = patientDao.getById(appointment.getTicket().getId());
                    timeMap.put(appointment.getTimeStart(), patient);
                }
            }

            fullMap.put(daySchedule.getDate().format(formatter), timeMap);
        }
        return fullMap;
    }
}
