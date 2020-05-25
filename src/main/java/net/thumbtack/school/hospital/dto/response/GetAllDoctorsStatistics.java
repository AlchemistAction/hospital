package net.thumbtack.school.hospital.dto.response;

import java.util.List;

public class GetAllDoctorsStatistics {

    private List<UserStatisticsDtoResponse> statistics;

    public GetAllDoctorsStatistics(List<UserStatisticsDtoResponse> statistics) {
        this.statistics = statistics;
    }

    public GetAllDoctorsStatistics() {
    }

    public List<UserStatisticsDtoResponse> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<UserStatisticsDtoResponse> statistics) {
        this.statistics = statistics;
    }
}
