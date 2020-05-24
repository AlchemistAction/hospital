package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.model.Doctor;

import java.util.Objects;

public class AddPatientToAppointmentDtoResponse {

    private String ticket;
    private int doctorId;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String speciality;
    private String room;
    private String date;
    private String time;

    public AddPatientToAppointmentDtoResponse() {
    }

    public AddPatientToAppointmentDtoResponse(String ticket, Doctor doctor, String date, String time) {
        this.ticket = ticket;
        this.doctorId = doctor.getId();
        this.firstName = doctor.getFirstName();
        this.lastName = doctor.getLastName();
        this.patronymic = doctor.getPatronymic();
        this.speciality = doctor.getSpeciality();
        this.room = doctor.getRoom();
        this.date = date;
        this.time = time;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddPatientToAppointmentDtoResponse)) return false;
        AddPatientToAppointmentDtoResponse that = (AddPatientToAppointmentDtoResponse) o;
        return doctorId == that.doctorId &&
                Objects.equals(getTicket(), that.getTicket()) &&
                Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getPatronymic(), that.getPatronymic()) &&
                Objects.equals(getSpeciality(), that.getSpeciality()) &&
                Objects.equals(getRoom(), that.getRoom()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getTime(), that.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTicket(), doctorId, getFirstName(), getLastName(), getPatronymic(), getSpeciality(), getRoom(), getDate(), getTime());
    }
}
