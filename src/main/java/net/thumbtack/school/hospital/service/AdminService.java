package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegisterAdminDtoResponse;

public interface AdminService {

    RegisterAdminDtoResponse registerAdmin(RegisterAdminDtoRequest registerAdminDtoRequest);

}
