package net.thumbtack.school.hospital.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.hospital.dto.internal.AppointmentForDto;
import net.thumbtack.school.hospital.dto.internal.DayScheduleForDto;
import net.thumbtack.school.hospital.dto.internal.WeekSchedule;
import net.thumbtack.school.hospital.dto.request.*;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.validator.ErrorModel;
import net.thumbtack.school.hospital.validator.GlobalErrorHandler;
import net.thumbtack.school.hospital.validator.exception.HospitalErrorCode;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptanceTest {

    private final RestTemplate template = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach()
    public void clearDatabase() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        template.setRequestFactory(factory);

        template.postForLocation("http://localhost:8080/api/debug/clear", null);
    }

    @Test
    public void acceptanceTest() throws JsonProcessingException {
        //login admin
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("SuperAdmin", "SuperAdminPassword");

        HttpHeaders requestHeadersForLogin = new HttpHeaders();
        HttpEntity requestEntityForLogin = new HttpEntity(loginDtoRequest, requestHeadersForLogin);
        HttpEntity responseEntityForLogin = template.postForEntity(
                "http://localhost:8080/api/sessions", requestEntityForLogin, HttpEntity.class);

        String cookie = responseEntityForLogin.getHeaders().get("Set-Cookie").get(0);

        //register first admin
        RegisterAdminDtoRequest dtoRequestForRegisterFirstAdmin = new RegisterAdminDtoRequest("рома",
                "ромагов", "романович", "regularAdmin", "adminLog",
                "adminPass");
        HttpHeaders requestHeadersForFirstAdmin = new HttpHeaders();
        requestHeadersForFirstAdmin.add("Cookie", cookie);
        HttpEntity requestEntityForRegisterFirstAdmin = new HttpEntity(
                dtoRequestForRegisterFirstAdmin, requestHeadersForFirstAdmin);

        ReturnAdminDtoResponse dtoResponseForRegisterFirstAdmin = template.postForObject(
                "http://localhost:8080/api/admins", requestEntityForRegisterFirstAdmin, ReturnAdminDtoResponse.class);
        assert dtoResponseForRegisterFirstAdmin != null;
        assertEquals(dtoResponseForRegisterFirstAdmin.getPosition(), dtoResponseForRegisterFirstAdmin.getPosition());

        //register second admin with null name
        RegisterAdminDtoRequest dtoRequestForRegisterSecondAdmin = new RegisterAdminDtoRequest(null,
                "стасов", null, "admin", "passss",
                "passss");
        HttpEntity requestEntityForRegisterSecondAdmin = new HttpEntity(
                dtoRequestForRegisterSecondAdmin, requestHeadersForFirstAdmin);

        try {
            template.postForObject(
                    "http://localhost:8080/api/admins", requestEntityForRegisterSecondAdmin, ReturnAdminDtoResponse.class);
            Assertions.fail();
        } catch (HttpClientErrorException exc) {
            Assertions.assertEquals(400, exc.getStatusCode().value());

            GlobalErrorHandler.MyError error = mapper.readValue(exc.getResponseBodyAsString(),
                    GlobalErrorHandler.MyError.class);
            ErrorModel errorModel = new ErrorModel("VALIDATION_ERROR", "firstName",
                    "Incorrect First name: ''");
            Assertions.assertEquals(errorModel, error.getAllErrors().get(0));
        }

        //register admin with wrong url 404
        try {
            template.postForObject(
                    "http://localhost:8080/api/adminss", requestEntityForRegisterSecondAdmin, ReturnAdminDtoResponse.class);
            Assertions.fail();
        } catch (HttpClientErrorException exc) {
            Assertions.assertEquals(404, exc.getStatusCode().value());

            GlobalErrorHandler.MyError error = mapper.readValue(exc.getResponseBodyAsString(),
                    GlobalErrorHandler.MyError.class);
            ErrorModel errorModel = new ErrorModel("ARGUMENT_MISMATCH_ERROR", "/api/adminss",
                    "No handler found for POST /api/adminss");
            Assertions.assertEquals(errorModel, error.getAllErrors().get(0));
        }

        //register admin with wrong url HTTP method
        try {
            template.delete(
                    "http://localhost:8080/api/admins", requestEntityForRegisterSecondAdmin, ReturnAdminDtoResponse.class);
            Assertions.fail();
        } catch (HttpClientErrorException exc) {
            Assertions.assertEquals(405, exc.getStatusCode().value());

            GlobalErrorHandler.MyError error = mapper.readValue(exc.getResponseBodyAsString(),
                    GlobalErrorHandler.MyError.class);
            ErrorModel errorModel = new ErrorModel("METHOD_NOT_ALLOWED", "DELETE",
                    "DELETE method is not supported for this request. Supported methods are " +
                            "POST PUT ");
            Assertions.assertEquals(errorModel, error.getAllErrors().get(0));
        }

        //logout first admin
        HttpEntity requestEntityForLogout = new HttpEntity(requestHeadersForFirstAdmin);
        template.execute("http://localhost:8080/api/sessions", HttpMethod.DELETE, null,
                null, requestEntityForLogout);


        //login with second admin
        LoginDtoRequest loginDtoRequestWithSecondAdmin = new LoginDtoRequest("adminLog", "adminPass");

        HttpHeaders requestHeadersLoginWithSecondAdmin = new HttpHeaders();
        HttpEntity requestEntityLoginWithSecondAdmin = new HttpEntity(
                loginDtoRequestWithSecondAdmin, requestHeadersLoginWithSecondAdmin);
        HttpEntity responseEntityLoginWithSecondAdmin = template.postForEntity(
                "http://localhost:8080/api/sessions", requestEntityLoginWithSecondAdmin, HttpEntity.class);

        String cookieLoginWithSecondAdmin = responseEntityLoginWithSecondAdmin.getHeaders().get("Set-Cookie").get(0);

        //register first doctor
        RegisterDoctorDtoRequest dtoRequestRegisterFirstDoctor = new RegisterDoctorDtoRequest("рома",
                "рома", "романов", "хирург", "100", "doctorLog",
                "doctorPa", "25-05-2020", "27-05-2020",
                new WeekSchedule("10:00", "12:00",
                        new String[]{"Monday", "Tuesday", "Friday"}), "00:30");

        HttpHeaders requestHeadersSecondAdmin = new HttpHeaders();
        requestHeadersSecondAdmin.add("Cookie", cookieLoginWithSecondAdmin);
        HttpEntity requestEntityRegisterFirstDoctor = new HttpEntity(
                dtoRequestRegisterFirstDoctor, requestHeadersSecondAdmin);

        ReturnDoctorDtoResponse dtoResponseRegisterFirstDoctor = template.postForObject(
                "http://localhost:8080/api/doctors", requestEntityRegisterFirstDoctor, ReturnDoctorDtoResponse.class);
        assert dtoResponseRegisterFirstDoctor != null;

        Map<String, List<AppointmentForDto>> resultMapScheduleOfFirstDoctor = new HashMap<>();
        List<AppointmentForDto> appList1 = Arrays.asList(
                new AppointmentForDto("10:00"), new AppointmentForDto("10:30"),
                new AppointmentForDto("11:00"), new AppointmentForDto("11:30"));
        resultMapScheduleOfFirstDoctor.put("25-05-2020", appList1);
        List<AppointmentForDto> appList2 = Arrays.asList(
                new AppointmentForDto("10:00"), new AppointmentForDto("10:30"),
                new AppointmentForDto("11:00"), new AppointmentForDto("11:30"));
        resultMapScheduleOfFirstDoctor.put("26-05-2020", appList2);
        assertEquals(resultMapScheduleOfFirstDoctor, dtoResponseRegisterFirstDoctor.getSchedule());

        //register second doctor
        RegisterDoctorDtoRequest dtoRequestRegisterSecondDoctor = new RegisterDoctorDtoRequest("рома",
                "рома", "романов", "лор", "200", "doctorLog1",
                "doctorPa1", "26-05-2020", "15-06-2020",
                new WeekSchedule("10:00", "13:00",
                        new String[]{"Monday"}), "00:20");
        HttpEntity requestEntityRegisterSecondDoctor = new HttpEntity(
                dtoRequestRegisterSecondDoctor, requestHeadersSecondAdmin);

        ReturnDoctorDtoResponse dtoResponseRegisterSecondDoctor = template.postForObject(
                "http://localhost:8080/api/doctors", requestEntityRegisterSecondDoctor, ReturnDoctorDtoResponse.class);
        assert dtoResponseRegisterSecondDoctor != null;
        assertEquals(dtoRequestRegisterSecondDoctor.getSpeciality(), dtoResponseRegisterSecondDoctor.getSpeciality());

        //register third doctor
        RegisterDoctorDtoRequest dtoRequestRegisterThirdDoctor = new RegisterDoctorDtoRequest("рома",
                "рома", "романов", "лор", "300", "doctorLog2",
                "doctorPa2", "26-05-2020", "15-06-2020",
                new WeekSchedule("10:00", "11:00",
                        new String[]{"Monday"}), "00:15");
        HttpEntity requestEntityRegisterThirdDoctor = new HttpEntity(
                dtoRequestRegisterThirdDoctor, requestHeadersSecondAdmin);

        ReturnDoctorDtoResponse dtoResponseRegisterThirdDoctor = template.postForObject(
                "http://localhost:8080/api/doctors", requestEntityRegisterThirdDoctor, ReturnDoctorDtoResponse.class);
        assert dtoResponseRegisterThirdDoctor != null;
        assertEquals(dtoRequestRegisterThirdDoctor.getRoom(), dtoResponseRegisterThirdDoctor.getRoom());

        //register fourth doctor
        RegisterDoctorDtoRequest dtoRequestRegisterFourthDoctor = new RegisterDoctorDtoRequest("рома",
                "рома", "романов", "хирург", "400", "doctorLog3",
                "doctorPa3", "25-05-2020", "27-05-2020",
                new DayScheduleForDto[]{
                        new DayScheduleForDto("Monday", "10:00", "12:00"),
                        new DayScheduleForDto("Friday", "11:00", "17:00")},
                "00:20");
        HttpEntity requestEntityRegisterFourthDoctor = new HttpEntity(
                dtoRequestRegisterFourthDoctor, requestHeadersSecondAdmin);

        ReturnDoctorDtoResponse dtoResponseRegisterFourthDoctor = template.postForObject(
                "http://localhost:8080/api/doctors", requestEntityRegisterFourthDoctor, ReturnDoctorDtoResponse.class);
        assert dtoResponseRegisterFourthDoctor != null;

        Map<String, List<AppointmentForDto>> resultMapScheduleOfFourthDoctor = new HashMap<>();
        List<AppointmentForDto> appList3 = Arrays.asList(
                new AppointmentForDto("10:00"), new AppointmentForDto("10:20"),
                new AppointmentForDto("10:40"), new AppointmentForDto("11:00"),
                new AppointmentForDto("11:20"), new AppointmentForDto("11:40"));
        resultMapScheduleOfFourthDoctor.put("25-05-2020", appList3);

        assertEquals(resultMapScheduleOfFourthDoctor, dtoResponseRegisterFourthDoctor.getSchedule());

        //register fifth doctor
        RegisterDoctorDtoRequest dtoRequestRegisterFifthDoctor = new RegisterDoctorDtoRequest("рома",
                "рома", "романов", "терапевт", "500", "doctorLog4",
                "doctorPa4", "26-05-2020", "15-06-2020",
                new DayScheduleForDto[]{
                        new DayScheduleForDto("Monday", "10:00", "14:00"),
                        new DayScheduleForDto("Tuesday", "11:00", "12:00")},
                "00:30");
        HttpEntity requestEntityRegisterFifthDoctor = new HttpEntity(
                dtoRequestRegisterFifthDoctor, requestHeadersSecondAdmin);

        ReturnDoctorDtoResponse dtoResponseRegisterFifthDoctor = template.postForObject(
                "http://localhost:8080/api/doctors", requestEntityRegisterFifthDoctor, ReturnDoctorDtoResponse.class);
        assert dtoResponseRegisterFifthDoctor != null;
        assertEquals(dtoRequestRegisterFifthDoctor.getFirstName(), dtoResponseRegisterFifthDoctor.getFirstName());

        //register doctor with wrong time format
        RegisterDoctorDtoRequest dtoRequestRegisterFifthDoctor33 = new RegisterDoctorDtoRequest("рома",
                "рома", "романов", "терапевт", "500", "doctorLog4",
                "doctorPa4", "26-05-2020", "15-06-2020",
                new DayScheduleForDto[]{
                        new DayScheduleForDto("Monday", "10:00", "14:00"),
                        new DayScheduleForDto("Tuesday", "11:00", "12:00")},
                "0030");
        HttpEntity requestEntityRegisterFifthDoctor33 = new HttpEntity(
                dtoRequestRegisterFifthDoctor33, requestHeadersSecondAdmin);
        try {
            template.postForObject(
                    "http://localhost:8080/api/doctors", requestEntityRegisterFifthDoctor33, ReturnDoctorDtoResponse.class);
            Assertions.fail();
        } catch (HttpServerErrorException exc) {
            Assertions.assertEquals(500, exc.getStatusCode().value());

            GlobalErrorHandler.MyError error = mapper.readValue(exc.getResponseBodyAsString(),
                    GlobalErrorHandler.MyError.class);
            ErrorModel errorModel = new ErrorModel("INTERNAL_SERVER_ERROR", "error occurred",
                    "Text '0030' could not be parsed at index 2");
            Assertions.assertEquals(errorModel.getMassage(), error.getAllErrors().get(0).getMassage());
        }


        //register new patient
        RegisterPatientDtoRequest dtoRequestRegisterFirstPatient = new RegisterPatientDtoRequest("роман",
                "романов", null, "patigtL", "1234dd5",
                "111@mail.ru", "c.Omsk", "89139992323");
        HttpHeaders requestHeadersRegisterFirstPatient = new HttpHeaders();
        HttpEntity requestEntityRegisterFirstPatient = new HttpEntity(
                dtoRequestRegisterFirstPatient, requestHeadersRegisterFirstPatient);

        HttpEntity responseEntityRegisterFirstPatient = template.postForEntity(
                "http://localhost:8080/api/patients", requestEntityRegisterFirstPatient, ReturnPatientDtoResponse.class);

        String cookieRegisterFirstPatient = responseEntityRegisterFirstPatient.getHeaders().get("Set-Cookie").get(0);

        ReturnPatientDtoResponse patient1 =
                (ReturnPatientDtoResponse) responseEntityRegisterFirstPatient.getBody();
        assert patient1 != null;
        assertEquals(dtoRequestRegisterFirstPatient.getEmail(), patient1.getEmail());

        //get settings with patient
        HttpHeaders requestHeadersFirstPatient = new HttpHeaders();
        requestHeadersFirstPatient.add("Cookie", cookieRegisterFirstPatient);
        HttpEntity requestEntityGetSettings = new HttpEntity(requestHeadersFirstPatient);

        ResponseEntity<SettingsDtoResponse> dtoResponseGetSettings = template.getForEntity(
                "http://localhost:8080/api/settings", SettingsDtoResponse.class, requestEntityGetSettings);

        SettingsDtoResponse settings = dtoResponseGetSettings.getBody();

        assert settings != null;
        assertEquals(12, settings.getMaxNameLength());
        assertEquals(5, settings.getMinPasswordLength());

        //get all doctors info with patient
        HttpEntity requestEntityGetAllDoctors = new HttpEntity(requestHeadersFirstPatient);

        GetAllDoctorsDtoResponse allDoctors = template.getForObject(
                "http://localhost:8080/api/doctors?schedule=yes",
                GetAllDoctorsDtoResponse.class, requestEntityGetAllDoctors);
        assert allDoctors != null;

        assertEquals(resultMapScheduleOfFourthDoctor, allDoctors.getList().get(3).getSchedule());

        //get first doctor info
        HttpEntity requestEntityGetDoctor = new HttpEntity(requestHeadersFirstPatient);
        int firstDoctorId = allDoctors.getList().get(0).getId();
        ReturnDoctorDtoResponse dtoResponseGetDoctor = template.getForObject(
                "http://localhost:8080/api/doctors/" + firstDoctorId
                        + "?schedule=yes&startDate=25-02-2020&endDate=25-05-2020",
                ReturnDoctorDtoResponse.class, requestEntityGetDoctor);
        assert dtoResponseGetDoctor != null;

        Map<String, List<AppointmentForDto>> resultMapScheduleOfFirstDoctor2 = new HashMap<>();
        List<AppointmentForDto> appList12 = Arrays.asList(
                new AppointmentForDto("10:00"), new AppointmentForDto("10:30"),
                new AppointmentForDto("11:00"), new AppointmentForDto("11:30"));
        resultMapScheduleOfFirstDoctor2.put("25-05-2020", appList12);

        assertEquals(resultMapScheduleOfFirstDoctor2, dtoResponseGetDoctor.getSchedule());

        //get first doctor info with incorrect paramType
        HttpEntity requestEntityGetDoctor1 = new HttpEntity(requestHeadersFirstPatient);
        try {
            template.getForObject(
                    "http://localhost:8080/api/doctors/" + "ccc"
                            + "?schedule=yes&startDate=25-02-2020&endDate=25-05-2020",
                    ReturnDoctorDtoResponse.class, requestEntityGetDoctor1);
            Assertions.fail();
        } catch (HttpClientErrorException exc) {
            Assertions.assertEquals(400, exc.getStatusCode().value());

            GlobalErrorHandler.MyError error = mapper.readValue(exc.getResponseBodyAsString(),
                    GlobalErrorHandler.MyError.class);
            ErrorModel errorModel = new ErrorModel("ARGUMENT_MISMATCH_ERROR", "doctorId",
                    "doctorId should be of type int");
            Assertions.assertEquals(errorModel, error.getAllErrors().get(0));
        }

        //update patient
        UpdatePatientDtoRequest updatePatientDtoRequest = new UpdatePatientDtoRequest("Стас",
                "романов", null, "111@mail.ru",
                "c.Omsk", "89139992323", "1234dd5",
                "1234dd56");
        HttpEntity requestEntityUpdatePatient = new HttpEntity(updatePatientDtoRequest, requestHeadersFirstPatient);

        ResponseEntity<ReturnPatientDtoResponse> responseEntityUpdatePatient = template.exchange(
                "http://localhost:8080/api/patients", HttpMethod.PUT,
                requestEntityUpdatePatient, ReturnPatientDtoResponse.class);
        ReturnPatientDtoResponse dtoResponseUpdatePatient = responseEntityUpdatePatient.getBody();
        assert dtoResponseUpdatePatient != null;
        assertEquals(updatePatientDtoRequest.getFirstName(), dtoResponseUpdatePatient.getFirstName());

        //make an appointment
        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest = new AddPatientToAppointmentDtoRequest(
                firstDoctorId, "25-05-2020", "11:30");

        HttpEntity requestEntityMakeAppointment = new HttpEntity(addPatientToAppointmentDtoRequest,
                requestHeadersFirstPatient);

        AddPatientToAppointmentDtoResponse dtoResponseMakeAppointment = template.postForObject(
                "http://localhost:8080/api/tickets", requestEntityMakeAppointment, AddPatientToAppointmentDtoResponse.class);
        assert dtoResponseMakeAppointment != null;
        assertEquals("D" + firstDoctorId + "202005251130", dtoResponseMakeAppointment.getTicket());

        //make five more appointments
        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest1 = new AddPatientToAppointmentDtoRequest(
                firstDoctorId, "26-05-2020", "11:00");
        HttpEntity requestEntityMakeAppointment1 = new HttpEntity(addPatientToAppointmentDtoRequest1,
                requestHeadersFirstPatient);
        template.postForObject("http://localhost:8080/api/tickets", requestEntityMakeAppointment1,
                AddPatientToAppointmentDtoResponse.class);

        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest2 = new AddPatientToAppointmentDtoRequest(
                allDoctors.getList().get(1).getId(), "01-06-2020", "10:00");
        HttpEntity requestEntityMakeAppointment2 = new HttpEntity(addPatientToAppointmentDtoRequest2,
                requestHeadersFirstPatient);
        template.postForObject("http://localhost:8080/api/tickets", requestEntityMakeAppointment2,
                AddPatientToAppointmentDtoResponse.class);

        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest3 = new AddPatientToAppointmentDtoRequest(
                allDoctors.getList().get(2).getId(), "08-06-2020", "10:15");
        HttpEntity requestEntityMakeAppointment3 = new HttpEntity(addPatientToAppointmentDtoRequest3,
                requestHeadersFirstPatient);
        template.postForObject("http://localhost:8080/api/tickets", requestEntityMakeAppointment3,
                AddPatientToAppointmentDtoResponse.class);

        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest4 = new AddPatientToAppointmentDtoRequest(
                allDoctors.getList().get(4).getId(), "02-06-2020", "11:00");
        HttpEntity requestEntityMakeAppointment4 = new HttpEntity(addPatientToAppointmentDtoRequest4,
                requestHeadersFirstPatient);
        template.postForObject("http://localhost:8080/api/tickets", requestEntityMakeAppointment4,
                AddPatientToAppointmentDtoResponse.class);

        //get all tickets
        HttpEntity requestEntityGetAllTickets = new HttpEntity(requestHeadersFirstPatient);
        GetAllTicketsDtoResponse allTickets = template.getForObject(
                "http://localhost:8080/api/tickets",
                GetAllTicketsDtoResponse.class, requestEntityGetAllTickets);
        assert allTickets != null;
        assertEquals(5, allTickets.getResponseList().size());

        //cancel appointment
        HttpEntity requestEntityDeleteTicket = new HttpEntity(requestHeadersFirstPatient);
        template.delete("http://localhost:8080/api/tickets/" + allTickets.getResponseList().get(0).getTicket(),
                requestEntityDeleteTicket);

        //get info about myself
        HttpEntity requestEntityGetInfo = new HttpEntity(requestHeadersFirstPatient);
        ReturnPatientDtoResponse dtoResponseGetInfo = template.getForObject(
                "http://localhost:8080/api/account",
                ReturnPatientDtoResponse.class, requestEntityGetInfo);
        assert dtoResponseGetInfo != null;
        assertEquals(patient1.getPhone(), dtoResponseGetInfo.getPhone());

        //logout first patient
        HttpEntity requestEntityForLogoutFirstPatient = new HttpEntity(requestHeadersFirstPatient);
        template.execute("http://localhost:8080/api/sessions", HttpMethod.DELETE, null,
                null, requestEntityForLogoutFirstPatient);


        //update doctor schedule with second admin
        UpdateScheduleDtoRequest updateScheduleDtoRequest = new UpdateScheduleDtoRequest(
                "29-05-2020",
                "01-06-2020",
                new DayScheduleForDto[]{
                        new DayScheduleForDto("Thursday", "10:00", "10:30"),
                        new DayScheduleForDto("Friday", "11:00", "11:30")},
                "00:30");

        HttpEntity requestEntityUpdateSchedule = new HttpEntity(updateScheduleDtoRequest, requestHeadersSecondAdmin);
        ResponseEntity<ReturnDoctorDtoResponse> responseEntityUpdateSchedule = template.exchange(
                "http://localhost:8080/api/doctors/" + firstDoctorId, HttpMethod.PUT,
                requestEntityUpdateSchedule, ReturnDoctorDtoResponse.class);
        ReturnDoctorDtoResponse dtoResponseUpdateSchedule = responseEntityUpdateSchedule.getBody();
        assert dtoResponseUpdateSchedule != null;
        assertEquals(3, dtoResponseUpdateSchedule.getSchedule().size());

        //delete doctor since date
        DeleteDoctorScheduleDtoRequest dtoRequestDeleteDoctor = new DeleteDoctorScheduleDtoRequest("28-05-2020");
        HttpEntity requestEntityDeleteDoctor = new HttpEntity(dtoRequestDeleteDoctor, requestHeadersSecondAdmin);
        HttpEntity response = template.exchange("http://localhost:8080/api/doctors/" + firstDoctorId,
                HttpMethod.DELETE, requestEntityDeleteDoctor, HttpEntity.class);

        //logout second admin
        HttpEntity requestEntityForLogout2 = new HttpEntity(requestHeadersSecondAdmin);
        template.execute("http://localhost:8080/api/sessions", HttpMethod.DELETE, null,
                null, requestEntityForLogout2);


        //register new patient
        RegisterPatientDtoRequest dtoRequestRegisterFirstPatient2 = new RegisterPatientDtoRequest("роман",
                "романов", null, "patigccL", "1234dd5",
                "111@mail.ru", "c.Omsk", "89139992323");
        HttpHeaders requestHeadersSecondPatient = new HttpHeaders();
        HttpEntity requestEntityRegisterFirstPatient2 = new HttpEntity(
                dtoRequestRegisterFirstPatient2, requestHeadersSecondPatient);

        HttpEntity responseEntityRegisterFirstPatient2 = template.postForEntity(
                "http://localhost:8080/api/patients", requestEntityRegisterFirstPatient2, ReturnPatientDtoResponse.class);

        String cookieRegisterSecondPatient = responseEntityRegisterFirstPatient2.getHeaders().get("Set-Cookie").get(0);
        requestHeadersSecondPatient.add("Cookie", cookieRegisterSecondPatient);

        ReturnPatientDtoResponse patient2 = (ReturnPatientDtoResponse) responseEntityRegisterFirstPatient2.getBody();

        //make an appointment with fail
        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest22 = new AddPatientToAppointmentDtoRequest(
                firstDoctorId, "25-05-2020", "11:30");
        HttpEntity requestEntityMakeAppointment22 = new HttpEntity(addPatientToAppointmentDtoRequest22,
                requestHeadersSecondPatient);
        try {
            template.postForObject("http://localhost:8080/api/tickets", requestEntityMakeAppointment22,
                    AddPatientToAppointmentDtoResponse.class);
            Assertions.fail();
        } catch (HttpClientErrorException exc) {
            Assertions.assertEquals(400, exc.getStatusCode().value());

            GlobalErrorHandler.MyError error = mapper.readValue(exc.getResponseBodyAsString(),
                    GlobalErrorHandler.MyError.class);
            ErrorModel errorModel = new ErrorModel(HospitalErrorCode.CAN_NOT_ADD_PATIENT_TO_APPOINTMENT,
                    "time", "Appointment on time: 11:30 is already occupied");
            Assertions.assertEquals(errorModel, error.getAllErrors().get(0));
        }

        //make five more appointments
        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest33 = new AddPatientToAppointmentDtoRequest(
                firstDoctorId, "26-05-2020", "10:30");
        HttpEntity requestEntityMakeAppointment33 = new HttpEntity(addPatientToAppointmentDtoRequest33,
                requestHeadersSecondPatient);
        template.postForObject("http://localhost:8080/api/tickets", requestEntityMakeAppointment33,
                AddPatientToAppointmentDtoResponse.class);

        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest44 = new AddPatientToAppointmentDtoRequest(
                allDoctors.getList().get(1).getId(), "01-06-2020", "10:20");
        HttpEntity requestEntityMakeAppointment44 = new HttpEntity(addPatientToAppointmentDtoRequest44,
                requestHeadersSecondPatient);
        template.postForObject("http://localhost:8080/api/tickets", requestEntityMakeAppointment44,
                AddPatientToAppointmentDtoResponse.class);

        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest55 = new AddPatientToAppointmentDtoRequest(
                allDoctors.getList().get(2).getId(), "08-06-2020", "10:30");
        HttpEntity requestEntityMakeAppointment55 = new HttpEntity(addPatientToAppointmentDtoRequest55,
                requestHeadersSecondPatient);
        template.postForObject("http://localhost:8080/api/tickets", requestEntityMakeAppointment55,
                AddPatientToAppointmentDtoResponse.class);

        AddPatientToAppointmentDtoRequest addPatientToAppointmentDtoRequest66 = new AddPatientToAppointmentDtoRequest(
                allDoctors.getList().get(4).getId(), "02-06-2020", "11:30");
        HttpEntity requestEntityMakeAppointment66 = new HttpEntity(addPatientToAppointmentDtoRequest66,
                requestHeadersSecondPatient);
        template.postForObject("http://localhost:8080/api/tickets", requestEntityMakeAppointment66,
                AddPatientToAppointmentDtoResponse.class);

        //logout second patient
        HttpEntity requestEntityForLogoutSecondPatient = new HttpEntity(requestHeadersSecondPatient);
        template.execute("http://localhost:8080/api/sessions", HttpMethod.DELETE, null,
                null, requestEntityForLogoutSecondPatient);


        //login with first doctor
        LoginDtoRequest loginDtoRequestDoctor = new LoginDtoRequest("doctorLog", "doctorPa");
        HttpHeaders requestHeadersLoginDoctor = new HttpHeaders();
        HttpEntity requestEntityLoginDoctor = new HttpEntity(
                loginDtoRequestDoctor, requestHeadersLoginDoctor);
        HttpEntity responseEntityLoginDoctor = template.postForEntity(
                "http://localhost:8080/api/sessions", requestEntityLoginDoctor, HttpEntity.class);
        String cookieLoginDoctor = responseEntityLoginDoctor.getHeaders().get("Set-Cookie").get(0);

        //create commission 1
        AddPatientToCommissionDtoRequest dtoRequestCommission1 = new AddPatientToCommissionDtoRequest(
                patient1.getId(), new Integer[]{allDoctors.getList().get(1).getId()}, "100",
                "01-06-2020", "15:15", "00:20");

        requestHeadersLoginDoctor.add("Cookie", cookieLoginDoctor);

        HttpEntity requestEntityCommission1 = new HttpEntity(dtoRequestCommission1,
                requestHeadersLoginDoctor);
        AddPatientToCommissionDtoResponse dtoResponseCommission1 = template.postForObject(
                "http://localhost:8080/api/commissions", requestEntityCommission1,
                AddPatientToCommissionDtoResponse.class);

        assert dtoResponseCommission1 != null;
        assertEquals("CD" + firstDoctorId + allDoctors.getList().get(1).getId() +
                "202006011515", dtoResponseCommission1.getTicket());

        //create commission 2
        AddPatientToCommissionDtoRequest dtoRequestCommission2 = new AddPatientToCommissionDtoRequest(
                patient1.getId(), new Integer[]{allDoctors.getList().get(2).getId()}, "100",
                "02-06-2020", "15:00", "00:40");

        HttpEntity requestEntityCommission2 = new HttpEntity(dtoRequestCommission2,
                requestHeadersLoginDoctor);
        AddPatientToCommissionDtoResponse dtoResponseCommission2 = template.postForObject(
                "http://localhost:8080/api/commissions", requestEntityCommission2,
                AddPatientToCommissionDtoResponse.class);

        assert dtoResponseCommission2 != null;
        assertEquals("CD" + firstDoctorId + allDoctors.getList().get(2).getId() +
                "202006021500", dtoResponseCommission2.getTicket());

        //create commission fail
        AddPatientToCommissionDtoRequest dtoRequestCommission3 = new AddPatientToCommissionDtoRequest(
                patient1.getId(), new Integer[]{allDoctors.getList().get(1).getId()}, "100",
                "01-06-2020", "10:25", "00:20");

        HttpEntity requestEntityCommission3 = new HttpEntity(dtoRequestCommission3,
                requestHeadersLoginDoctor);
        try {
            AddPatientToCommissionDtoResponse dtoResponseCommission3 = template.postForObject(
                    "http://localhost:8080/api/commissions", requestEntityCommission3,
                    AddPatientToCommissionDtoResponse.class);
            Assertions.fail();
        } catch (HttpClientErrorException exc) {
            Assertions.assertEquals(400, exc.getStatusCode().value());

            GlobalErrorHandler.MyError error = mapper.readValue(exc.getResponseBodyAsString(),
                    GlobalErrorHandler.MyError.class);
            ErrorModel errorModel = new ErrorModel("CAN_NOT_ADD_PATIENT_TO_COMMISSION", "doctorIds",
                    "Doctor with ID: " + allDoctors.getList().get(1).getId() + "" +
                            " has no free time for commission");
            Assertions.assertEquals(errorModel, error.getAllErrors().get(0));
        }


        //get tickets with first patient
        LoginDtoRequest loginDtoRequest2 = new LoginDtoRequest("patigtL", "1234dd56");
        HttpHeaders requestHeadersLogin2 = new HttpHeaders();
        HttpEntity requestEntityLogin2 = new HttpEntity(
                loginDtoRequest2, requestHeadersLogin2);
        HttpEntity responseEntityLogin2 = template.postForEntity(
                "http://localhost:8080/api/sessions", requestEntityLogin2, HttpEntity.class);

        String cookieLogin2 = responseEntityLogin2.getHeaders().get("Set-Cookie").get(0);
        HttpHeaders headersPatient1 = new HttpHeaders();
        headersPatient1.add("Cookie", cookieLogin2);

        HttpEntity requestEntityGetAllTickets2 = new HttpEntity(headersPatient1);
        GetAllTicketsDtoResponse allTickets2 = template.getForObject(
                "http://localhost:8080/api/tickets",
                GetAllTicketsDtoResponse.class, requestEntityGetAllTickets2);
        assert allTickets2 != null;
        assertEquals(6, allTickets2.getResponseList().size());

        //cancel commission
        HttpEntity requestEntityCancelCommission = new HttpEntity(requestHeadersLogin2);
        HttpEntity response2 = template.exchange("http://localhost:8080/api/commissions/"
                        + dtoResponseCommission2.getTicket(),
                HttpMethod.DELETE, requestEntityCancelCommission, HttpEntity.class);


        //login first admin
        LoginDtoRequest loginDtoRequest3 = new LoginDtoRequest("SuperAdmin", "SuperAdminPassword");

        HttpHeaders requestHeadersForLogin3 = new HttpHeaders();
        HttpEntity requestEntityForLogin3 = new HttpEntity(loginDtoRequest3, requestHeadersForLogin3);
        HttpEntity responseEntityForLogin3 = template.postForEntity(
                "http://localhost:8080/api/sessions", requestEntityForLogin3, HttpEntity.class);

        String cookie3 = responseEntityForLogin3.getHeaders().get("Set-Cookie").get(0);

        //get patient statistics
        HttpHeaders requestHeadersForFirstAdmin3 = new HttpHeaders();
        requestHeadersForFirstAdmin3.add("Cookie", cookie3);
        HttpEntity requestEntityForRegisterFirstAdmin3 = new HttpEntity(requestHeadersForFirstAdmin3);

        PatientStatisticsDtoResponse patientStatisticsDtoResponse = template.getForObject(
                "http://localhost:8080/api/statistics/patients/" + patient1.getId(),
                PatientStatisticsDtoResponse.class, requestEntityForRegisterFirstAdmin3);
        assert patientStatisticsDtoResponse != null;
        List<String> statistics = Arrays.asList(
                "1 appointment(s) to Doctors with speciality: терапевт",
                "1 appointment(s) to Doctors with speciality: хирург",
                "2 appointment(s) to Doctors with speciality: лор");
        assertEquals(statistics, patientStatisticsDtoResponse.getStatistics());
    }
}