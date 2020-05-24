package net.thumbtack.school.hospital.dto.request;


import net.thumbtack.school.hospital.validator.annotations.Phone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RegisterPatientDtoRequest extends RegisterUserDtoRequest {

    @Email
    private String email;
    @NotBlank
    @NotNull
    private String address;
    @Phone(field = "Phone")
    private String phone;

    public RegisterPatientDtoRequest(String firstName, String lastName, String patronymic, String login,
                                     String password, String email, String address, String phone) {
        super(firstName, lastName, patronymic, login, password);
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public RegisterPatientDtoRequest() {
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
}
