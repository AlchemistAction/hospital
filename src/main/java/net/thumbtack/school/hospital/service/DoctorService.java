package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.RegisterDoctorDtoResponse;

public interface DoctorService {

    RegisterDoctorDtoResponse registerDoctor(RegisterDoctorDtoRequest registerDoctorDtoRequest);
}
