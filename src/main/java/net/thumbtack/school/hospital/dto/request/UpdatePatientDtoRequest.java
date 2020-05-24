package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validator.annotations.Name;
import net.thumbtack.school.hospital.validator.annotations.Password;
import net.thumbtack.school.hospital.validator.annotations.Patronymic;
import net.thumbtack.school.hospital.validator.annotations.Phone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdatePatientDtoRequest {

    @Name(field = "First name")
    private String firstName;
    @Name(field = "Last name")
    private String lastName;
    @Patronymic(field = "Patronymic")
    private String patronymic;
    @Email
    private String email;
    @NotBlank
    @NotNull
    private String address;
    @Phone(field = "Phone")
    private String phone;
    private String oldPassword;
    @Password
    private String newPassword;

    public UpdatePatientDtoRequest() {
    }

    public UpdatePatientDtoRequest(String firstName, String lastName, String patronymic, String email, String address,
                                   String phone, String oldPassword, String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
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

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
