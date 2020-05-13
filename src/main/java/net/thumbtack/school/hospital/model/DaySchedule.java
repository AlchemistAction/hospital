package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DaySchedule {

    private int id;
    private LocalDate date;
    private Doctor doctor;
    private List<Appointment> appointmentList;

    public DaySchedule() {
    }

    public DaySchedule(int id, LocalDate date, List<Appointment> appointmentList) {
        this.id = id;
        this.date = date;
        this.appointmentList = appointmentList;
    }

    public DaySchedule(LocalDate date, List<Appointment> appointmentList) {
        this(0, date, appointmentList);
    }

    public LocalDate getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DaySchedule)) return false;
        DaySchedule that = (DaySchedule) o;
        return getId() == that.getId() &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getDoctor(), that.getDoctor()) &&
                Objects.equals(getAppointmentList(), that.getAppointmentList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getDoctor(), getAppointmentList());
    }
}
