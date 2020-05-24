package net.thumbtack.school.hospital.dto.response;

import java.util.List;

public class GetAllDoctorsDtoResponse {

    private List<ReturnDoctorDtoResponse> list;

    public GetAllDoctorsDtoResponse(List<ReturnDoctorDtoResponse> list) {
        this.list = list;
    }

    public GetAllDoctorsDtoResponse() {
    }

    public List<ReturnDoctorDtoResponse> getList() {
        return list;
    }

    public void setList(List<ReturnDoctorDtoResponse> list) {
        this.list = list;
    }
}
