package net.thumbtack.school.hospital.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.thumbtack.school.hospital.dto.internal.DoctorInfo;

import java.util.List;
import java.util.Objects;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GetTicketDtoResponse {

    private String ticket;
    private String room;
    private String date;
    private String time;
    private List<DoctorInfo> list;
    private Integer doctorId;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String speciality;


    public GetTicketDtoResponse() {
    }

    public GetTicketDtoResponse(String ticket, String room, String date, String time, Integer doctorId,
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

    public GetTicketDtoResponse(String ticket, String room, String date, String time, List<DoctorInfo> list) {
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

    public List<DoctorInfo> getList() {
        return list;
    }

    public void setList(List<DoctorInfo> list) {
        this.list = list;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetTicketDtoResponse)) return false;
        GetTicketDtoResponse that = (GetTicketDtoResponse) o;
        return Objects.equals(getTicket(), that.getTicket()) &&
                Objects.equals(getRoom(), that.getRoom()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getTime(), that.getTime()) &&
                Objects.equals(getList(), that.getList()) &&
                Objects.equals(getDoctorId(), that.getDoctorId()) &&
                Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getPatronymic(), that.getPatronymic()) &&
                Objects.equals(getSpeciality(), that.getSpeciality());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTicket(), getRoom(), getDate(), getTime(), getList(), getDoctorId(), getFirstName(), getLastName(), getPatronymic(), getSpeciality());
    }
}
