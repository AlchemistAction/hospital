package net.thumbtack.school.hospital.model.exception;

public enum HospitalErrorCode {

    DUPLICATE_USER("User has already registered"),
    DOCTOR_NOT_WORKING_ON_THAT_DAY("Doctor doesn't work on that day"),
    CAN_NOT_ADD_PATIENT_TO_APPOINTMENT("Can't add patient to appointment"),
    ALL_APPOINTMENTS_ARE_FULL("All appointments on that day are already occupied"),
    CAN_NOT_ADD_PATIENT_TO_COMMISSION("Can't add patient to commission"),
    CAN_NOT_UPDATE_SCHEDULE("All dates are already occupied");

    private String errorString;

    HospitalErrorCode(String errorString) {
        this.errorString = errorString;
    }

    private String getErrorString() {
        return errorString;
    }
}
