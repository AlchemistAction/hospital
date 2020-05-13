package net.thumbtack.school.hospital.dto.internal;

public class DayScheduleForDto {

    private String weekDay;
    private String timeStart;
    private String timeEnd;

    public DayScheduleForDto(String weekDay, String timeStart, String timeEnd) {
        this.weekDay = weekDay;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public DayScheduleForDto() {
    }

    public String getWeekDay() {
        return weekDay;
    }


    public String getTimeStart() {
        return timeStart;
    }


    public String getTimeEnd() {
        return timeEnd;
    }

}
