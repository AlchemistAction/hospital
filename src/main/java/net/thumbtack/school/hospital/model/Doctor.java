package net.thumbtack.school.hospital.model;

import java.util.*;

public class Doctor extends User {

    private String speciality;
    private String room;
    private List<DaySchedule> schedule;
    private List<Commission> commissionList;

    public Doctor() {
    }

    public Doctor(int id, UserType userType, String firstName, String lastName, String patronymic, String login,
                  String password, String speciality, String room, List<DaySchedule> schedule) {
        super(id, userType, firstName, lastName, patronymic, login, password);
        this.speciality = speciality;
        this.room = room;
        this.schedule = schedule;
    }

    public Doctor(int id, UserType userType, String firstName, String lastName, String patronymic, String login,
                  String password, String speciality, String room,
                  List<DaySchedule> schedule, List<Commission> commissionList) {
        super(id, userType, firstName, lastName, patronymic, login, password);
        this.speciality = speciality;
        this.room = room;
        this.schedule = schedule;
        this.commissionList = commissionList;
    }

    public Doctor(int id, UserType userType, String firstName, String lastName, String patronymic, String login,
                  String password, String speciality, String room) {
        this(id, userType, firstName, lastName, patronymic, login, password,
                speciality, room, new ArrayList<>(), new ArrayList<>());
    }

    public Doctor(UserType userType, String firstName, String lastName, String patronymic, String login,
                  String password, String speciality, String room, List<DaySchedule> schedule) {
        this(0, userType, firstName, lastName, patronymic, login, password, speciality, room, schedule);
    }

    public Doctor(UserType userType, String firstName, String lastName, String patronymic, String login,
                  String password, String speciality, String room,
                  List<DaySchedule> schedule, List<Commission> commissionList) {
        this(0, userType, firstName, lastName, patronymic, login, password,
                speciality, room, schedule, commissionList);
    }

    public Doctor(UserType userType, String firstName, String lastName, String patronymic, String login,
                  String password, String speciality, String room) {
        this(0, userType, firstName, lastName, patronymic, login, password, speciality, room,
                new ArrayList<>(), new ArrayList<>());
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

    public List<DaySchedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<DaySchedule> schedule) {
        this.schedule = schedule;
    }

    public List<Commission> getCommissionList() {
        return commissionList;
    }

    public void setCommissionList(List<Commission> commissionList) {
        this.commissionList = commissionList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        if (!super.equals(o)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(getSpeciality(), doctor.getSpeciality()) &&
                Objects.equals(getRoom(), doctor.getRoom()) &&
                Objects.equals(getSchedule(), doctor.getSchedule()) &&
                Objects.equals(getCommissionList(), doctor.getCommissionList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSpeciality(), getRoom(), getSchedule(), getCommissionList());
    }
}
