package net.thumbtack.school.hospital.model.exception;

public enum HospitalErrorCode {

    DUPLICATE_USER("User has already registered"),
    WRONG_LOGIN("Wrong login"),
    WRONG_PASSWORD("Wrong password"),
    CAN_NOT_ADD_PATIENT_TO_APPOINTMENT("Can't add patient to appointment"),
    CAN_NOT_ADD_PATIENT_TO_COMMISSION("Can't add patient to commission"),
    CAN_NOT_FIND_USER("Can't find user"),
    CAN_NOT_UPDATE_SCHEDULE("All dates are already occupied");

    private String errorString;

    HospitalErrorCode(String errorString) {
        this.errorString = errorString;
    }

    private String getErrorString() {
        return errorString;
    }
}
