package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class Commission {

    private int id;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String room;
    private List<Doctor> doctorList;
    private Ticket ticket;

    public Commission() {
    }

    public Commission(int id, LocalDate date, LocalTime timeStart, LocalTime timeEnd, String room,
                      List<Doctor> doctorList, Ticket ticket) {
        this.id = id;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.room = room;
        this.doctorList = doctorList;
        this.ticket = ticket;
    }

    public Commission(LocalDate date, LocalTime timeStart, LocalTime timeEnd, String room, List<Doctor> doctorList,
                      Ticket ticket) {
        this(0, date, timeStart, timeEnd, room, doctorList, ticket);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commission)) return false;
        Commission that = (Commission) o;
        return getId() == that.getId() &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getTimeStart(), that.getTimeStart()) &&
                Objects.equals(getTimeEnd(), that.getTimeEnd()) &&
                Objects.equals(getRoom(), that.getRoom()) &&
                Objects.equals(getDoctorList(), that.getDoctorList()) &&
                Objects.equals(getTicket(), that.getTicket());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getTimeStart(), getTimeEnd(), getRoom(), getDoctorList(), getTicket());
    }
}
