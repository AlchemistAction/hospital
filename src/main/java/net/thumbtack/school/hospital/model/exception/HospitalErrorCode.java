package net.thumbtack.school.hospital.model.exception;

public enum HospitalErrorCode {

    DUPLICATE_USER("User has already registered"),
    CAN_NOT_UPDATE_SCHEDULE("All dates are already occupied");

    private String errorString;

    HospitalErrorCode(String errorString) {
        this.errorString = errorString;
    }

    private String getErrorString() {
        return errorString;
    }
}
