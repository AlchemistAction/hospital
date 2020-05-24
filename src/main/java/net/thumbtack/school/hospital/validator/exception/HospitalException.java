package net.thumbtack.school.hospital.validator.exception;

import net.thumbtack.school.hospital.validator.ErrorModel;

public class HospitalException extends Exception {

    private final ErrorModel errorModel;

    public HospitalException(ErrorModel errorModel) {
        super();
        this.errorModel = errorModel;
    }

    public ErrorModel getErrorModel() {
        return errorModel;
    }
}
