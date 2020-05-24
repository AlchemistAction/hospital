package net.thumbtack.school.hospital.validator;

import net.thumbtack.school.hospital.validator.exception.HospitalErrorCode;
import org.springframework.validation.FieldError;

import java.util.Objects;

public class ErrorModel {

    private String errorCode;
    private String field;
    private String massage;

    public ErrorModel(String errorCode, String field, String massage) {
        this.errorCode = errorCode;
        this.field = field;
        this.massage = massage;
    }

    public ErrorModel(HospitalErrorCode hospitalErrorCode, String field, String massage) {
        this.errorCode = hospitalErrorCode.toString();
        this.field = field;
        this.massage = massage;
    }

    public ErrorModel(FieldError fieldError, HospitalErrorCode hospitalErrorCode) {
        this.errorCode = hospitalErrorCode.name();
        this.field = fieldError.getField();
        this.massage = fieldError.getDefaultMessage();
    }

    public ErrorModel() {
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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
        if (!(o instanceof ErrorModel)) return false;
        ErrorModel that = (ErrorModel) o;
        return Objects.equals(getErrorCode(), that.getErrorCode()) &&
                Objects.equals(getField(), that.getField()) &&
                Objects.equals(getMassage(), that.getMassage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getErrorCode(), getField(), getMassage());
    }
}
