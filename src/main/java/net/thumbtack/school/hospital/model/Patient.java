package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Patient extends User {
    private String email;
    private String address;
    private String phone;


    public Patient(int id, UserType userType, String firstName, String lastName, String patronymic,
                   String login, String password, String email, String address, String phone) {
        super(id, userType, firstName, lastName, patronymic, login, password);
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public Patient(UserType userType, String firstName, String lastName, String patronymic,
                   String login, String password, String email, String address, String phone) {
        super(userType, firstName, lastName, patronymic, login, password);
        this.email = email;
        this.address = address;
        this.phone = phone;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        if (!super.equals(o)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(getEmail(), patient.getEmail()) &&
                Objects.equals(getAddress(), patient.getAddress()) &&
                Objects.equals(getPhone(), patient.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEmail(), getAddress(), getPhone());
    }
}
