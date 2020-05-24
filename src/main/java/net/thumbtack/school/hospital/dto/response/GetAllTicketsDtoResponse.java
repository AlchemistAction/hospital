package net.thumbtack.school.hospital.dto.response;

import java.util.List;

public class GetAllTicketsDtoResponse {
    private List<GetTicketDtoResponse> responseList;

    public GetAllTicketsDtoResponse(List<GetTicketDtoResponse> responseList) {
        this.responseList = responseList;
    }

    public GetAllTicketsDtoResponse() {
    }

    public List<GetTicketDtoResponse> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<GetTicketDtoResponse> responseList) {
        this.responseList = responseList;
    }
}
