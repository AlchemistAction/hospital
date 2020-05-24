package net.thumbtack.school.hospital.dto.internal;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppointmentForDto {

    private String time;
    private Integer id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String address;
    private String phone;
    private String massage;

    public AppointmentForDto(String time) {
        this.time = time;
    }

    public AppointmentForDto(String time, String massage) {
        this.time = time;
        this.massage = massage;
    }

    public AppointmentForDto(String time, Integer id, String firstName, String lastName, String patronymic,
                             String email, String address, String phone) {
        this.time = time;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public AppointmentForDto() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentForDto)) return false;
        AppointmentForDto that = (AppointmentForDto) o;
        return Objects.equals(getTime(), that.getTime()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getPatronymic(), that.getPatronymic()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getAddress(), that.getAddress()) &&
                Objects.equals(getPhone(), that.getPhone()) &&
                Objects.equals(getMassage(), that.getMassage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTime(), getId(), getFirstName(), getLastName(), getPatronymic(), getEmail(), getAddress(), getPhone(), getMassage());
    }
}
