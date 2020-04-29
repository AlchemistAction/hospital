package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Ticket {

    private int id;
    private String name;
    private Patient patient;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return getId() == ticket.getId() &&
                Objects.equals(getName(), ticket.getName()) &&
                Objects.equals(getPatient(), ticket.getPatient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPatient());
    }
}
