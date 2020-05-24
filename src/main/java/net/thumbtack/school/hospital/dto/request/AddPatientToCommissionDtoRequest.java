package net.thumbtack.school.hospital.dto.request;

public class AddPatientToCommissionDtoRequest {

    private int patientId;
    private Integer[] doctorIds;
    private String room;
    private String date;
    private String time;
    private String duration;

    public AddPatientToCommissionDtoRequest() {
    }

    public AddPatientToCommissionDtoRequest(int patientId, Integer[] doctorIds, String room, String date, String time,
                                            String duration) {
        this.patientId = patientId;
        this.doctorIds = doctorIds;
        this.room = room;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public int getPatientId() {
        return patientId;
    }

    public Integer[] getDoctorIds() {
        return doctorIds;
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
}
