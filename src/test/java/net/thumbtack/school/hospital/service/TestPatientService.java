package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.request.RegisterPatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.RegisterPatientDtoResponse;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.mybatis.dao.PatientDao;
import net.thumbtack.school.hospital.mybatis.daoimpl.PatientDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestPatientService {

    private PatientDao patientDao;
    private PatientService patientService;
    private ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        patientDao = mock(PatientDaoImpl.class);
        patientService = new PatientService(patientDao);
    }

    @Test
    public void testRegisterPatient() {
        RegisterPatientDtoRequest registerPatientDtoRequest = new RegisterPatientDtoRequest("name",
                "surname", "patronymic", "patientLogin", "password111",
                "111@mail.ru", "c.Omsk", "232323");

        Patient patient = modelMapper.map(registerPatientDtoRequest, Patient.class);
        patient.setId(2);

        when(patientDao.insert(any())).thenReturn(patient);

        RegisterPatientDtoResponse registerPatientDtoResponse = patientService.registerPatient(registerPatientDtoRequest);

        assertEquals(2, registerPatientDtoResponse.getId());
    }

}
