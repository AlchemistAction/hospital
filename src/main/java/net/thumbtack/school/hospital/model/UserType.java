package net.thumbtack.school.hospital.model;

public enum UserType {
    ADMIN("admin"),
    DOCTOR("doctor"),
    PATIENT("patient");

    private String userType;

    UserType(String userType) {
        this.userType = userType;
    }

    private String getUserType() {
        return userType;
    }
}
