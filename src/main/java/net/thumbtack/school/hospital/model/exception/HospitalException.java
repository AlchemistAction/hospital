package net.thumbtack.school.hospital.model.exception;

public class HospitalException extends Exception {

    private HospitalErrorCode error;

    public HospitalException(HospitalErrorCode error) {
        super(error.name());
        this.error = error;
    }

    public HospitalErrorCode getErrorCode() {
        return error;
    }
}
