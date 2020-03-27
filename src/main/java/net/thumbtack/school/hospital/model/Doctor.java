package net.thumbtack.school.hospital.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Doctor extends User {

    private String speciality;
    private String room;
    private String dateStart;
    private String dateEnd;
    private List<DaySchedule> weekDaysSchedule;

    public Doctor(int id, UserType userType, String firstName, String lastName, String patronymic, String login,
                  String password, String speciality, String room, String dateStart, String dateEnd) {
        super(id, userType, firstName, lastName, patronymic, login, password);
        this.speciality = speciality;
        this.room = room;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weekDaysSchedule = new ArrayList<>();
    }

    public Doctor(UserType userType, String firstName, String lastName, String patronymic, String login, String password,
                  String speciality, String room, String dateStart, String dateEnd) {
    	// REVU this(0, ...);
       super(userType, firstName, lastName, patronymic, login, password);
        this.speciality = speciality;
        this.room = room;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weekDaysSchedule = new ArrayList<>();
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

    public List<DaySchedule> getWeekDaysSchedule() {
        return weekDaysSchedule;
    }

    public void setWeekDaysSchedule(List<DaySchedule> weekDaysSchedule) {
        this.weekDaysSchedule = weekDaysSchedule;
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
                Objects.equals(getWeekDaysSchedule(), doctor.getWeekDaysSchedule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSpeciality(), getRoom(), getDateStart(), getDateEnd(), getWeekDaysSchedule());
    }
}
