package net.thumbtack.school.hospital.dto.response;

import java.util.Objects;

public class ReturnAdminDtoResponse extends ReturnUserDtoResponse {

    private String position;

    public ReturnAdminDtoResponse() {
    }

    public ReturnAdminDtoResponse(int id, String firstName, String lastName, String patronymic, String position) {
        super(id, firstName, lastName, patronymic);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReturnAdminDtoResponse)) return false;
        if (!super.equals(o)) return false;
        ReturnAdminDtoResponse that = (ReturnAdminDtoResponse) o;
        return Objects.equals(getPosition(), that.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPosition());
    }
}
