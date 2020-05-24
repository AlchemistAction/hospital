package net.thumbtack.school.hospital.dto.response;

import java.util.Arrays;
import java.util.Objects;

public class AddPatientToCommissionDtoResponse {

    private String ticket;
    private int patientId;
    private Integer[] doctorIds;
    private String room;
    private String date;
    private String time;
    private String duration;

    public AddPatientToCommissionDtoResponse(String ticket, int patientId, Integer[] doctorIds, String room, String date,
                                             String time, String duration) {
        this.ticket = ticket;
        this.patientId = patientId;
        this.doctorIds = doctorIds;
        this.room = room;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public AddPatientToCommissionDtoResponse() {
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public Integer[] getDoctorIds() {
        return doctorIds;
    }

    public void setDoctorIds(Integer[] doctorIds) {
        this.doctorIds = doctorIds;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddPatientToCommissionDtoResponse)) return false;
        AddPatientToCommissionDtoResponse that = (AddPatientToCommissionDtoResponse) o;
        return getPatientId() == that.getPatientId() &&
                Objects.equals(getTicket(), that.getTicket()) &&
                Arrays.equals(getDoctorIds(), that.getDoctorIds()) &&
                Objects.equals(getRoom(), that.getRoom()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getTime(), that.getTime()) &&
                Objects.equals(getDuration(), that.getDuration());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getTicket(), getPatientId(), getRoom(), getDate(), getTime(), getDuration());
        result = 31 * result + Arrays.hashCode(getDoctorIds());
        return result;
    }
}
