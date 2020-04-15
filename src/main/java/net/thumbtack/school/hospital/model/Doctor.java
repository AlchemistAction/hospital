package net.thumbtack.school.hospital.model;

import java.util.*;

public class Doctor extends User {

    private String speciality;
    private String room;
    private String dateStart;
    private String dateEnd;
    private List<DaySchedule> schedule;

    public Doctor(int id, UserType userType, String firstName, String lastName, String patronymic, String login,
                  String password, String speciality, String room, String dateStart, String dateEnd,
                  List<DaySchedule> schedule) {
        super(id, userType, firstName, lastName, patronymic, login, password);
        this.speciality = speciality;
        this.room = room;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.schedule = schedule;
    }

    public Doctor(int id, UserType userType, String firstName, String lastName, String patronymic, String login,
                  String password, String speciality, String room, String dateStart, String dateEnd) {
        this(id, userType, firstName, lastName, patronymic, login, password, speciality, room,
                dateStart, dateEnd, new ArrayList<>());
    }

    public Doctor(UserType userType, String firstName, String lastName, String patronymic, String login,
                  String password, String speciality, String room, String dateStart, String dateEnd,
                  List<DaySchedule> schedule) {
        this(0, userType, firstName, lastName, patronymic, login, password, speciality, room,
                dateStart, dateEnd, schedule);
    }

    public Doctor(UserType userType, String firstName, String lastName, String patronymic, String login,
                  String password, String speciality, String room, String dateStart, String dateEnd) {
        this(0, userType, firstName, lastName, patronymic, login, password, speciality, room,
                dateStart, dateEnd, new ArrayList<>());
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

    public List<DaySchedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<DaySchedule> schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        if (!super.equals(o)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(getSpeciality(), doctor.getSpeciality()) &&
                Objects.equals(getRoom(), doctor.getRoom()) &&
                Objects.equals(getDateStart(), doctor.getDateStart()) &&
                Objects.equals(getDateEnd(), doctor.getDateEnd()) &&
                Objects.equals(getSchedule(), doctor.getSchedule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSpeciality(), getRoom(), getDateStart(), getDateEnd(), getSchedule());
    }
}
