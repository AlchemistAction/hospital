package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.model.Patient;

import java.util.Map;
import java.util.Objects;

public class ReturnDoctorDtoResponse extends ReturnUserDtoResponse {


    private String speciality;
    private String room;
    private Map<String, Map<String, Patient>> schedule;

    public ReturnDoctorDtoResponse() {
    }

    public ReturnDoctorDtoResponse(int id, String firstName, String lastName, String patronymic, String speciality,
                                   String room, Map<String, Map<String, Patient>> schedule) {
        super(id, firstName, lastName, patronymic);
        this.speciality = speciality;
        this.room = room;
        this.schedule = schedule;
    }

    public ReturnDoctorDtoResponse(int id, String firstName, String lastName, String patronymic, String speciality,
                                   String room) {
        super(id, firstName, lastName, patronymic);
        this.speciality = speciality;
        this.room = room;

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

    public Map<String, Map<String, Patient>> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<String, Map<String, Patient>> schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReturnDoctorDtoResponse)) return false;
        if (!super.equals(o)) return false;
        ReturnDoctorDtoResponse that = (ReturnDoctorDtoResponse) o;
        return Objects.equals(getSpeciality(), that.getSpeciality()) &&
                Objects.equals(getRoom(), that.getRoom()) &&
                Objects.equals(getSchedule(), that.getSchedule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSpeciality(), getRoom(), getSchedule());
    }
}
