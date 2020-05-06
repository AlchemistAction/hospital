package net.thumbtack.school.hospital.dto.response;

import java.util.Objects;

public class ReturnPatientDtoResponse extends ReturnUserDtoResponse {


    private String email;
    private String address;
    private String phone;

    public ReturnPatientDtoResponse() {
    }

    public ReturnPatientDtoResponse(int id, String firstName, String lastName, String patronymic, String email,
                                    String address, String phone) {
        super(id, firstName, lastName, patronymic);
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
        if (!(o instanceof ReturnPatientDtoResponse)) return false;
        if (!super.equals(o)) return false;
        ReturnPatientDtoResponse that = (ReturnPatientDtoResponse) o;
        return Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getAddress(), that.getAddress()) &&
                Objects.equals(getPhone(), that.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEmail(), getAddress(), getPhone());
    }
}
