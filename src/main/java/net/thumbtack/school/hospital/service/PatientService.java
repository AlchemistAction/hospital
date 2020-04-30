package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegisterPatientDtoResponse;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.mybatis.dao.PatientDao;
import org.modelmapper.ModelMapper;

public class PatientService {

    private PatientDao patientDao;
    private ModelMapper modelMapper = new ModelMapper();

    public PatientService(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    public RegisterPatientDtoResponse registerPatient(RegisterPatientDtoRequest registerPatientDtoRequest) {
        Patient patient = modelMapper.map(registerPatientDtoRequest, Patient.class);

        patient = patientDao.insert(patient);

        return modelMapper.map(patient, RegisterPatientDtoResponse.class);
    }
}
