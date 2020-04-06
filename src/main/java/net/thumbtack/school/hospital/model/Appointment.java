package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Appointment {

    private String dateOfAppointment;
    private String timeStart;
    private String timeEnd;
    private boolean isFree;
    private boolean isLockedForCommission;

    public Appointment(String dateOfAppointment, String timeStart, String timeEnd, boolean isFree,
                       boolean isLockedForCommission) {
        this.dateOfAppointment = dateOfAppointment;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.isFree = isFree;
        this.isLockedForCommission = isLockedForCommission;
    }

    public String getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(String dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public boolean isLockedForCommission() {
        return isLockedForCommission;
    }

    public void setLockedForCommission(boolean lockedForCommission) {
        isLockedForCommission = lockedForCommission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        Appointment that = (Appointment) o;
        return isFree() == that.isFree() &&
                isLockedForCommission() == that.isLockedForCommission() &&
                Objects.equals(getDateOfAppointment(), that.getDateOfAppointment()) &&
                Objects.equals(getTimeStart(), that.getTimeStart()) &&
                Objects.equals(getTimeEnd(), that.getTimeEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDateOfAppointment(), getTimeStart(), getTimeEnd(), isFree(), isLockedForCommission());
    }
}
