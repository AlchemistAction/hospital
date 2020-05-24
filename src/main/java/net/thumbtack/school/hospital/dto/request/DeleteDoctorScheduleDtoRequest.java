package net.thumbtack.school.hospital.dto.request;

public class DeleteDoctorScheduleDtoRequest {

    private String date;

    public DeleteDoctorScheduleDtoRequest() {
    }

    public DeleteDoctorScheduleDtoRequest(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
