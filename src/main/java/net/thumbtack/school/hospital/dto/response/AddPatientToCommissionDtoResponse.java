package net.thumbtack.school.hospital.dto.response;

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
}
