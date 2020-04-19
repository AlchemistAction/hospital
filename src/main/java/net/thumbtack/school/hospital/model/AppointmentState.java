package net.thumbtack.school.hospital.model;

public enum AppointmentState {
    FREE("Free"),
    APPOINTMENT("Locked for Appointment"),
    COMMISSION("Locked for Commission");

    private String state;

    AppointmentState(String state) {
        this.state = state;
    }

    private String getState() {
        return state;
    }
}