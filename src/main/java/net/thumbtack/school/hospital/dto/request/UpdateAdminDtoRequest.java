package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validator.annotations.Name;
import net.thumbtack.school.hospital.validator.annotations.Password;
import net.thumbtack.school.hospital.validator.annotations.Patronymic;

public class UpdateAdminDtoRequest {

    @Name(field = "First name")
    private String firstName;
    @Name(field = "Last name")
    private String lastName;
    @Patronymic(field = "Patronymic")
    private String patronymic;
    private String position;
    private String oldPassword;
    @Password
    private String newPassword;

    public UpdateAdminDtoRequest() {
    }

    public UpdateAdminDtoRequest(String firstName, String lastName, String patronymic, String position,
                                 String oldPassword, String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
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

    public String getPosition() {
        return position;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
