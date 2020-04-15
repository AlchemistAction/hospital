package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Appointment {

    private int id;
    private String timeStart;
    private String timeEnd;
    private AppointmentState state;
    private String ticket;

    public Appointment(int id, String timeStart, String timeEnd, AppointmentState state) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.state = state;
    }

    public Appointment(int id, String timeStart, String timeEnd, AppointmentState state, String ticket) {
        this(id, timeStart, timeEnd, state);
        this.ticket = ticket;
    }

    public Appointment(String timeStart, String timeEnd, AppointmentState state) {
        this(0, timeStart, timeEnd, state);
    }

    public Appointment(String timeStart, String timeEnd, AppointmentState state, String ticket) {
        this(0, timeStart, timeEnd, state, ticket);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public AppointmentState getState() {
        return state;
    }

    public void setState(AppointmentState state) {
        this.state = state;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        Appointment that = (Appointment) o;
        return getId() == that.getId() &&
                Objects.equals(getTimeStart(), that.getTimeStart()) &&
                Objects.equals(getTimeEnd(), that.getTimeEnd()) &&
                getState() == that.getState() &&
                Objects.equals(getTicket(), that.getTicket());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTimeStart(), getTimeEnd(), getState(), getTicket());
    }
}
