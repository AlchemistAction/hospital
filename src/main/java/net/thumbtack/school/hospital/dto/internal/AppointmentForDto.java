package net.thumbtack.school.hospital.dto.internal;

import java.util.Objects;

public class AppointmentForDto {

    private String time;
    private PatientInfo  patientInfo;

    public AppointmentForDto(String time, PatientInfo patientInfo) {
        this.time = time;
        this.patientInfo = patientInfo;
    }

    public AppointmentForDto(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public PatientInfo getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfo patientInfo) {
        this.patientInfo = patientInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentForDto)) return false;
        AppointmentForDto that = (AppointmentForDto) o;
        return Objects.equals(getTime(), that.getTime()) &&
                Objects.equals(getPatientInfo(), that.getPatientInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTime(), getPatientInfo());
    }
}
