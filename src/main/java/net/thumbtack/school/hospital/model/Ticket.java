package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Ticket {

    private int id;
    private String name;
    private int patientId;
    private int doctorId;

    public Ticket(int id, String name, int patientId, int doctorId) {
        this.id = id;
        this.name = name;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

    public Ticket(String name, int patientId, int doctorId) {
        this(0, name, patientId, doctorId);
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

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return getId() == ticket.getId() &&
                getDoctorId() == ticket.getDoctorId() &&
                getPatientId() == ticket.getPatientId() &&
                Objects.equals(getName(), ticket.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDoctorId(), getPatientId());
    }
}
