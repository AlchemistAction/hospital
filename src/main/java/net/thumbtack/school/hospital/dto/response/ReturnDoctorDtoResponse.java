package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.model.Patient;

import java.util.Map;

public class ReturnDoctorDtoResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String speciality;
    private String room;
    private Map<String, Map<String, Patient>> schedule;

    public ReturnDoctorDtoResponse() {
    }

    public ReturnDoctorDtoResponse(int id, String firstName, String lastName, String patronymic, String speciality,
                                   String room, Map<String, Map<String, Patient>> schedule) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.speciality = speciality;
        this.room = room;
        this.schedule = schedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Map<String, Map<String, Patient>> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<String, Map<String, Patient>> schedule) {
        this.schedule = schedule;
    }
}
