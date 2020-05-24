package net.thumbtack.school.hospital.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RegisterAdminDtoRequest extends RegisterUserDtoRequest {

    @NotNull
    @NotBlank
    private String position;

    public RegisterAdminDtoRequest(String firstName, String lastName, String patronymic, String position,
                                   String login, String password) {
        super(firstName, lastName, patronymic, login, password);
        this.position = position;
    }

    public RegisterAdminDtoRequest() {
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
