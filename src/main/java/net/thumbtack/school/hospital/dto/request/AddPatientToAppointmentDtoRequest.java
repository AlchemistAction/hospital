package net.thumbtack.school.hospital.dto.request;

public class AddPatientToAppointmentDtoRequest {

    private int doctorId;
    private String speciality;
    private String date;
    private String time;

    public AddPatientToAppointmentDtoRequest(String speciality, String date, String time) {
        this.speciality = speciality;
        this.date = date;
        this.time = time;
    }

    public AddPatientToAppointmentDtoRequest(int doctorId, String date, String time) {
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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
}
