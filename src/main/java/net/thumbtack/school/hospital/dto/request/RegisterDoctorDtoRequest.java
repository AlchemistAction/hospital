package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.dto.internal.DayScheduleForDto;
import net.thumbtack.school.hospital.dto.internal.WeekSchedule;
import net.thumbtack.school.hospital.validator.Name;

public class RegisterDoctorDtoRequest {

    @Name
    private String firstName;
    @Name
    private String lastName;
    private String patronymic;
    private String speciality;
    private String room;
    private String login;
    private String password;
    private String dateStart;
    private String dateEnd;
    private WeekSchedule weekSchedule;
    private DayScheduleForDto[] weekDaysSchedule;
    private String duration;

    public RegisterDoctorDtoRequest(String firstName, String lastName, String patronymic, String speciality,
                                    String room, String login, String password, String dateStart, String dateEnd,
                                    WeekSchedule weekSchedule, String duration) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.speciality = speciality;
        this.room = room;
        this.login = login;
        this.password = password;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weekSchedule = weekSchedule;
        this.duration = duration;
    }

    public RegisterDoctorDtoRequest(String firstName, String lastName, String patronymic, String speciality,
                                    String room, String login, String password, String dateStart, String dateEnd,
                                    DayScheduleForDto[] weekDaysSchedule, String duration) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.speciality = speciality;
        this.room = room;
        this.login = login;
        this.password = password;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weekDaysSchedule = weekDaysSchedule;
        this.duration = duration;
    }

    public RegisterDoctorDtoRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
