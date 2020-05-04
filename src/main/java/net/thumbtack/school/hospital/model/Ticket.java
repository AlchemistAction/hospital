package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Ticket {

    private int id;
    private String name;
    private Patient patient;
    private Appointment appointment;
    private Commission commission;

    public Ticket() {
    }

    public Ticket(int id, String name, Patient patient) {
        this.id = id;
        this.name = name;
        this.patient = patient;
    }

    public Ticket(String name, Patient patient) {
        this(0, name, patient);
    }

    public Ticket(int id, String name, Patient patient, Appointment appointment) {
        this(id, name, patient);
        this.appointment = appointment;
    }

    public Ticket(String name, Patient patient, Appointment appointment) {
        this(0, name, patient);
        this.appointment = appointment;
    }

    public Ticket(int id, String name, Patient patient, Commission commission) {
        this(id, name, patient);
        this.commission = commission;
    }

    public Ticket(String name, Patient patient, Commission commission) {
        this(0, name, patient);
        this.commission = commission;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                Objects.equals(getName(), ticket.getName()) &&
                Objects.equals(getPatient(), ticket.getPatient()) &&
                Objects.equals(getAppointment(), ticket.getAppointment()) &&
                Objects.equals(getCommission(), ticket.getCommission());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPatient(), getAppointment(), getCommission());
    }
}
