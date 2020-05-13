package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.dto.internal.DayScheduleForDto;
import net.thumbtack.school.hospital.dto.internal.WeekSchedule;

public class UpdateScheduleDtoRequest {

    private String dateStart;
    private String dateEnd;
    private WeekSchedule weekSchedule;
    private DayScheduleForDto[] weekDaysSchedule;
    private String duration;

    public UpdateScheduleDtoRequest(String dateStart, String dateEnd, WeekSchedule weekSchedule, String duration) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weekSchedule = weekSchedule;
        this.duration = duration;
    }

    public UpdateScheduleDtoRequest(String dateStart, String dateEnd, DayScheduleForDto[] weekDaysSchedule,
                                    String duration) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weekDaysSchedule = weekDaysSchedule;
        this.duration = duration;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public WeekSchedule getWeekSchedule() {
        return weekSchedule;
    }

    public DayScheduleForDto[] getWeekDaysSchedule() {
        return weekDaysSchedule;
    }

    public String getDuration() {
        return duration;
    }
}
