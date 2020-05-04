package net.thumbtack.school.hospital.model;

import java.util.List;
import java.util.Objects;

public class Commission {

    private int id;
    private List<Appointment> appointmentList;
    private String room;
    private Ticket ticket;

    public Commission() {
    }

    public Commission(int id, List<Appointment> appointmentList, String room, Ticket ticket) {
        this.id = id;
        this.appointmentList = appointmentList;
        this.room = room;
        this.ticket = ticket;
    }

    public Commission(List<Appointment> appointmentList, String room, Ticket ticket) {
        this(0, appointmentList, room, ticket);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commission)) return false;
        Commission that = (Commission) o;
        return getId() == that.getId() &&
                Objects.equals(getAppointmentList(), that.getAppointmentList()) &&
                Objects.equals(getRoom(), that.getRoom()) &&
                Objects.equals(getTicket(), that.getTicket());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAppointmentList(), getRoom(), getTicket());
    }
}
