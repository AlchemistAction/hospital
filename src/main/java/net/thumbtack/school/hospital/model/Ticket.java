package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Ticket {

    private int id;
    private String number;
    private Patient patient;
    private Appointment appointment;
    private Commission commission;

    public Ticket() {
    }

    public Ticket(int id, String number, Patient patient) {
        this.id = id;
        this.number = number;
        this.patient = patient;
    }

    public Ticket(String number, Patient patient) {
        this(0, number, patient);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Commission getCommission() {
        return commission;
    }

    public void setCommission(Commission commission) {
        this.commission = commission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return getId() == ticket.getId() &&
                Objects.equals(getNumber(), ticket.getNumber()) &&
                Objects.equals(getPatient(), ticket.getPatient()) &&
                Objects.equals(getAppointment(), ticket.getAppointment()) &&
                Objects.equals(getCommission(), ticket.getCommission());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNumber(), getPatient(), getAppointment(), getCommission());
    }
}
