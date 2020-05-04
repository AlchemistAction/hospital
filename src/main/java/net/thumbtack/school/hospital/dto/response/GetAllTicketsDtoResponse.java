package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.dto.internal.DoctorInfo;

import java.util.List;
import java.util.Objects;

public class GetAllTicketsDtoResponse {

    private String ticket;
    private String room;
    private String date;
    private String time;

    private int doctorId;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String speciality;

    private List<DoctorInfo> list;

    public GetAllTicketsDtoResponse(String ticket, String room, String date, String time, int doctorId,
                                    String firstName, String lastName, String patronymic, String speciality) {
        this.ticket = ticket;
        this.room = room;
        this.date = date;
        this.time = time;
        this.doctorId = doctorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.speciality = speciality;
    }

    public GetAllTicketsDtoResponse(String ticket, String room, String date, String time, List<DoctorInfo> list) {
        this.ticket = ticket;
        this.room = room;
        this.date = date;
        this.time = time;
        this.list = list;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
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

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
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

    public List<DoctorInfo> getDoctorList() {
        return list;
    }

    public void setDoctorList(List<DoctorInfo> list) {
        this.list = list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetAllTicketsDtoResponse)) return false;
        GetAllTicketsDtoResponse response = (GetAllTicketsDtoResponse) o;
        return getDoctorId() == response.getDoctorId() &&
                Objects.equals(getTicket(), response.getTicket()) &&
                Objects.equals(getRoom(), response.getRoom()) &&
                Objects.equals(getDate(), response.getDate()) &&
                Objects.equals(getTime(), response.getTime()) &&
                Objects.equals(getFirstName(), response.getFirstName()) &&
                Objects.equals(getLastName(), response.getLastName()) &&
                Objects.equals(getPatronymic(), response.getPatronymic()) &&
                Objects.equals(getSpeciality(), response.getSpeciality()) &&
                Objects.equals(list, response.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTicket(), getRoom(), getDate(), getTime(), getDoctorId(), getFirstName(), getLastName(), getPatronymic(), getSpeciality(), list);
    }
}
