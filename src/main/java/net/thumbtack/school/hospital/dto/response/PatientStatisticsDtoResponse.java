package net.thumbtack.school.hospital.dto.response;

import java.util.List;

public class PatientStatisticsDtoResponse {
    private List<String> statistics;

    public PatientStatisticsDtoResponse(List<String> statistics) {
        this.statistics = statistics;
    }

    public PatientStatisticsDtoResponse() {
    }

    public List<String> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<String> statistics) {
        this.statistics = statistics;
    }
}
