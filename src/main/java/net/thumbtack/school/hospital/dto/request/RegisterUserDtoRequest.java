package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validator.annotations.Login;
import net.thumbtack.school.hospital.validator.annotations.Name;
import net.thumbtack.school.hospital.validator.annotations.Password;
import net.thumbtack.school.hospital.validator.annotations.Patronymic;

public abstract class RegisterUserDtoRequest {

    @Name(field = "First name")
    private String firstName;
    @Name(field = "Last name")
    private String lastName;
    @Patronymic(field = "Patronymic")
    private String patronymic;
    @Login(field = "Login")
    private String login;
    @Password(field = "Password")
    private String password;

    public RegisterUserDtoRequest(String firstName, String lastName, String patronymic, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
    }

    public RegisterUserDtoRequest() {
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
