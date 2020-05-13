package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validator.Name;

import javax.validation.constraints.NotNull;

public class RegisterAdminDtoRequest {


    @Name(field = "First name")
    private String firstName;
    @Name(field = "Last name")
    private String lastName;

    private String patronymic;
    @NotNull
    private String position;
    @NotNull
    private String login;
    @NotNull
    private String password;

    public RegisterAdminDtoRequest(String firstName, String lastName, String patronymic, String position, String login,
                                   String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
        this.login = login;
        this.password = password;
    }

    public RegisterAdminDtoRequest() {
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

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
