package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.dto.internal.DayScheduleForDto;
import net.thumbtack.school.hospital.dto.internal.WeekSchedule;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RegisterDoctorDtoRequest extends RegisterUserDtoRequest {

    @NotNull
    @NotBlank
    private String speciality;
    @NotNull
    @NotBlank
    private String room;
    @NotNull
    @NotBlank
    private String dateStart;
    @NotNull
    @NotBlank
    private String dateEnd;
    private WeekSchedule weekSchedule;
    private DayScheduleForDto[] weekDaysSchedule;
    @NotNull
    @NotBlank
    private String duration;

    public RegisterDoctorDtoRequest(String firstName, String lastName, String patronymic, String speciality,
                                    String room, String login, String password, String dateStart, String dateEnd,
                                    WeekSchedule weekSchedule, String duration) {
        super(firstName, lastName, patronymic, login, password);
        this.speciality = speciality;
        this.room = room;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weekSchedule = weekSchedule;
        this.duration = duration;
    }

    public RegisterDoctorDtoRequest(String firstName, String lastName, String patronymic, String speciality,
                                    String room, String login, String password, String dateStart, String dateEnd,
                                    DayScheduleForDto[] weekDaysSchedule, String duration) {
        super(firstName, lastName, patronymic, login, password);
        this.speciality = speciality;
        this.room = room;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weekDaysSchedule = weekDaysSchedule;
        this.duration = duration;
    }

    public RegisterDoctorDtoRequest() {
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public WeekSchedule getWeekSchedule() {
        return weekSchedule;
    }

    public void setWeekSchedule(WeekSchedule weekSchedule) {
        this.weekSchedule = weekSchedule;
    }

    public DayScheduleForDto[] getWeekDaysSchedule() {
        return weekDaysSchedule;
    }

    public void setWeekDaysSchedule(DayScheduleForDto[] weekDaysSchedule) {
        this.weekDaysSchedule = weekDaysSchedule;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
