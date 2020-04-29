package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.request.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegisterDoctorDtoResponse;

public interface DoctorService {

    RegisterDoctorDtoResponse registerDoctor(RegisterDoctorDtoRequest registerDoctorDtoRequest);
}
