package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.RegisterAdminDtoResponse;

public interface AdminService {

    RegisterAdminDtoResponse registerAdmin(RegisterAdminDtoRequest registerAdminDtoRequest);

}
