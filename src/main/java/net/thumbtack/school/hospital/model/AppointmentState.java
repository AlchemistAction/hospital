package net.thumbtack.school.hospital.model;

public enum AppointmentState {
    IS_FREE("isFree"),
    IS_LOCKED_FOR_APPOINTMENT("isLockedForAppointment"),
    IS_LOCKED_FOR_COMMISSION("isLockedForCommission");

    private String state;

    AppointmentState(String state) {
        this.state = state;
    }

    private String getState() {
        return state;
    }
}