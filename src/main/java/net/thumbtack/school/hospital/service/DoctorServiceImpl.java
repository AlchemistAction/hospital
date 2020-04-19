package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.RegisterDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.internal.DayScheduleForDto;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.mybatis.dao.DoctorDao;
import net.thumbtack.school.hospital.mybatis.dao.PatientDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DoctorServiceImpl implements DoctorService {

    private DoctorDao doctorDao;
    private PatientDao patientDao;

    public DoctorServiceImpl(DoctorDao doctorDao, PatientDao patientDao) {
        this.doctorDao = doctorDao;
        this.patientDao = patientDao;
    }

    @Override
    public RegisterDoctorDtoResponse registerDoctor(RegisterDoctorDtoRequest registerDoctorDtoRequest) {

        List<DaySchedule> schedule = createSchedule(registerDoctorDtoRequest);

        Doctor doctor = new Doctor(UserType.DOCTOR, registerDoctorDtoRequest.getFirstName(),
                registerDoctorDtoRequest.getLastName(), registerDoctorDtoRequest.getPatronymic(),
                registerDoctorDtoRequest.getLogin(), registerDoctorDtoRequest.getPassword(),
                registerDoctorDtoRequest.getSpeciality(), registerDoctorDtoRequest.getRoom(),
                registerDoctorDtoRequest.getDateStart(), registerDoctorDtoRequest.getDateEnd(), schedule);

        doctor = doctorDao.insert(doctor);

        Map<String, Map<String, Patient>> scheduleForResponse = createScheduleForResponse(doctor.getSchedule());

        return new RegisterDoctorDtoResponse(doctor.getId(), doctor.getFirstName(), doctor.getLastName(),
                doctor.getPatronymic(), doctor.getSpeciality(), doctor.getRoom(), scheduleForResponse);
    }


    private List<DaySchedule> createSchedule(RegisterDoctorDtoRequest registerDoctorDtoRequest) {

        List<LocalDate> dateListForAllPeriod = getDatesForAllPeriod(registerDoctorDtoRequest);

        if (registerDoctorDtoRequest.getWeekSchedule() != null) {
            return getScheduleFromWeekSchedule(registerDoctorDtoRequest, dateListForAllPeriod);
        } else {
            return getScheduleFromWeekDaysSchedule(registerDoctorDtoRequest, dateListForAllPeriod);
        }
    }

    private List<DaySchedule> getScheduleFromWeekDaysSchedule(RegisterDoctorDtoRequest registerDoctorDtoRequest,
                                                              List<LocalDate> dateListForAllPeriod) {

        List<DaySchedule> schedule = new ArrayList<>();

        for (DayScheduleForDto dayScheduleForDto : registerDoctorDtoRequest.getWeekDaysSchedule()) {

            for (LocalDate date : dateListForAllPeriod) {
                if (date.getDayOfWeek().name().equals(dayScheduleForDto.getWeekDay())) {
                    List<Appointment> appointmentList = getAppointments(registerDoctorDtoRequest);

                    DaySchedule daySchedule = new DaySchedule(date, appointmentList);
                    schedule.add(daySchedule);
                }
            }
        }
        return schedule;
    }

    private List<DaySchedule> getScheduleFromWeekSchedule(RegisterDoctorDtoRequest registerDoctorDtoRequest,
                                                          List<LocalDate> dateListForAllPeriod) {

        List<DaySchedule> schedule = new ArrayList<>();

        for (String dateString : registerDoctorDtoRequest.getWeekSchedule().getWeekDays()) {
            for (LocalDate date : dateListForAllPeriod) {
                if (date.getDayOfWeek().name().toLowerCase().equals(dateString.toLowerCase())) {

                    List<Appointment> appointmentList = getAppointments(registerDoctorDtoRequest);

                    DaySchedule daySchedule = new DaySchedule(date, appointmentList);
                    schedule.add(daySchedule);
                }
            }
        }
        return schedule;
    }

    private List<Appointment> getAppointments(RegisterDoctorDtoRequest registerDoctorDtoRequest) {

        LocalTime start = LocalTime.parse(registerDoctorDtoRequest.getWeekSchedule().getTimeStart());
        LocalTime end = LocalTime.parse(registerDoctorDtoRequest.getWeekSchedule().getTimeEnd());
        LocalTime duration = LocalTime.parse(registerDoctorDtoRequest.getDuration());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

        List<Appointment> appointmentList = new ArrayList<>();

        for (LocalTime time = start; time.isBefore(end); time = time.plusMinutes(duration.getMinute())) {

            Appointment appointment = new Appointment(time.format(dtf),
                    time.plusMinutes(duration.getMinute()).format(dtf), AppointmentState.FREE);

            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    private List<LocalDate> getDatesForAllPeriod(RegisterDoctorDtoRequest registerDoctorDtoRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate start = LocalDate.parse(registerDoctorDtoRequest.getDateStart(), formatter);
        LocalDate end = LocalDate.parse(registerDoctorDtoRequest.getDateEnd(), formatter);

        List<LocalDate> dateList = new ArrayList<>();

        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            if (!date.getDayOfWeek().name().equals("Saturday") && !date.getDayOfWeek().name().equals("Sunday"))
                dateList.add(date);
        }
        return dateList;
    }

    private Map<String, Map<String, Patient>> createScheduleForResponse(List<DaySchedule> schedule) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Map<String, Map<String, Patient>> fullMap = new HashMap<>();

        for (DaySchedule daySchedule : schedule) {

            Map<String, Patient> timeMap = new HashMap<>();

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
