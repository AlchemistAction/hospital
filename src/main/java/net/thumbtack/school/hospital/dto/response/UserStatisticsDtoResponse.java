package net.thumbtack.school.hospital.dto.response;

import java.util.List;

public class UserStatisticsDtoResponse {
    private List<String> statistics;

    public UserStatisticsDtoResponse(List<String> statistics) {
        this.statistics = statistics;
    }

    public UserStatisticsDtoResponse() {
    }

    public List<String> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<String> statistics) {
        this.statistics = statistics;
    }
}
