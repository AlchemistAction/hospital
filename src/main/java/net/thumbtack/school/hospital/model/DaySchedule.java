package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DaySchedule {

    private int id;
    private LocalDate dateOfAppointment;
    private List<Appointment> appointmentList;

    public DaySchedule(int id, LocalDate dateOfAppointment, List<Appointment> appointmentList) {
        this.id = id;
        this.dateOfAppointment = dateOfAppointment;
        this.appointmentList = appointmentList;
    }

    public DaySchedule(int id, LocalDate dateOfAppointment) {
        this(id, dateOfAppointment, new ArrayList<>());
    }

    public DaySchedule(LocalDate dateOfAppointment, List<Appointment> appointmentList) {
        this(0, dateOfAppointment, appointmentList);
    }

    public DaySchedule(LocalDate dateOfAppointment) {
        this(0, dateOfAppointment, new ArrayList<>());
    }

    public LocalDate getDateOfAppointment() {
        return dateOfAppointment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDateOfAppointment(LocalDate dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
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
                Objects.equals(getDateOfAppointment(), that.getDateOfAppointment()) &&
                Objects.equals(getAppointmentList(), that.getAppointmentList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDateOfAppointment(), getAppointmentList());
    }
}
