package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Appointment {

    private int id;
    private String timeStart;
    private String timeEnd;
    private AppointmentState state;
    private DaySchedule daySchedule;
    private Ticket ticket;

    public Appointment() {
    }

    public Appointment(int id, String timeStart, String timeEnd, AppointmentState state, DaySchedule daySchedule) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.state = state;
        this.daySchedule = daySchedule;
    }

    public Appointment(int id, String timeStart, String timeEnd, AppointmentState state) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.state = state;
    }

    public Appointment(int id, String timeStart, String timeEnd, AppointmentState state,
                       DaySchedule daySchedule, Ticket ticket) {
        this(id, timeStart, timeEnd, state, daySchedule);
        this.ticket = ticket;
    }

    public Appointment(String timeStart, String timeEnd, AppointmentState state, DaySchedule daySchedule) {
        this(0, timeStart, timeEnd, state, daySchedule);
    }

    public Appointment(String timeStart, String timeEnd, AppointmentState state) {
        this(0, timeStart, timeEnd, state);
    }

    public Appointment(String timeStart, String timeEnd, AppointmentState state, DaySchedule daySchedule, Ticket ticket) {
        this(0, timeStart, timeEnd, state, daySchedule, ticket);

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

    public DaySchedule getDaySchedule() {
        return daySchedule;
    }

    public void setDaySchedule(DaySchedule daySchedule) {
        this.daySchedule = daySchedule;
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
        if (!(o instanceof Appointment)) return false;
        Appointment that = (Appointment) o;
        return getId() == that.getId() &&
                Objects.equals(getTimeStart(), that.getTimeStart()) &&
                Objects.equals(getTimeEnd(), that.getTimeEnd()) &&
                getState() == that.getState() &&
                Objects.equals(getDaySchedule(), that.getDaySchedule()) &&
                Objects.equals(getTicket(), that.getTicket());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTimeStart(), getTimeEnd(), getState(), getDaySchedule(), getTicket());
    }
}
