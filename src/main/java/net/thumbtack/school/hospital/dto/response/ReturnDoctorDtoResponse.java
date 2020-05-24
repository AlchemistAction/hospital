package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.dto.internal.AppointmentForDto;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReturnDoctorDtoResponse extends ReturnUserDtoResponse {

    private String speciality;
    private String room;
    private Map<String, List<AppointmentForDto>> schedule;

    public ReturnDoctorDtoResponse() {
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

    public Map<String, List<AppointmentForDto>> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<String, List<AppointmentForDto>> schedule) {
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
