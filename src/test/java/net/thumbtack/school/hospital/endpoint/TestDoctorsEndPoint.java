package net.thumbtack.school.hospital.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.hospital.dao.dao.DoctorDao;
import net.thumbtack.school.hospital.dto.internal.DayScheduleForDto;
import net.thumbtack.school.hospital.dto.internal.WeekSchedule;
import net.thumbtack.school.hospital.dto.request.DeleteDoctorScheduleDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateScheduleDtoRequest;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.UserService;
import net.thumbtack.school.hospital.validator.ErrorModel;
import net.thumbtack.school.hospital.validator.GlobalErrorHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DoctorsEndPoint.class)
@ComponentScan("net.thumbtack.school.hospital")
public class TestDoctorsEndPoint {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private DoctorDao doctorDao;
    @MockBean
    private DoctorService doctorService;
    @MockBean
    private UserService userService;

    @Test
    public void testRegisterDoctor() throws Exception {

        RegisterDoctorDtoRequest registerDoctorDtoRequest = new RegisterDoctorDtoRequest("рома",
                "рома", "романов", "хирург", "100", "doctorLog",
                "doctorPa", "13-04-2020", "16-04-2020",
                new WeekSchedule("10:00", "11:00", new String[]{"Monday", "Friday"}), "00:30");

        List<DaySchedule> schedule = Collections.singletonList(
                new DaySchedule(LocalDate.of(2020, 4, 13),
                        Arrays.asList(
                                new Appointment(LocalTime.parse("10:00"), LocalTime.parse("10:30"), AppointmentState.FREE),
                                new Appointment(LocalTime.parse("10:30"), LocalTime.parse("11:00"), AppointmentState.FREE))));

        Doctor doctor = modelMapper.map(registerDoctorDtoRequest, Doctor.class);
        doctor.setSchedule(schedule);
        doctor.setId(3);

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.ADMIN);
        when(doctorDao.insert(any())).thenReturn(doctor);

        MvcResult result = mvc.perform(post("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(registerDoctorDtoRequest)))
                .andReturn();

        assertEquals(result.getResponse().getStatus(), 200);
    }

    @Test
    public void testRegisterDoctorFail() throws Exception {

        RegisterDoctorDtoRequest registerDoctorDtoRequest = new RegisterDoctorDtoRequest("рома",
                "рома", "романов", "хирург", "100", "doctorLog",
                "doctorPa", "13-04-2020", "16-04-2020",
                new WeekSchedule("10:00", "11:00", new String[]{"Monday", "Friday"}), "00:30");

        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.PATIENT);

        MvcResult result = mvc.perform(post("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(registerDoctorDtoRequest)))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(),
                GlobalErrorHandler.MyError.class);

        ErrorModel errorModel = new ErrorModel("WRONG_USER_TYPE", "no field",
                "Only admins are allowed to register other doctors");
        assertEquals(errorModel, error.getAllErrors().get(0));
    }

    @Test
    public void testGetDoctorFail() throws Exception {
        mvc.perform(get("/api/doctors/123")).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllDoctorsFail() throws Exception {
        mvc.perform(get("/api/doctors")).andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateDoctorsFail() throws Exception {

        UpdateScheduleDtoRequest updateScheduleDtoRequest = new UpdateScheduleDtoRequest(
                "15-04-2020",
                "18-04-2020",
                new DayScheduleForDto[]{
                        new DayScheduleForDto("Thursday", "10:00", "10:30"),
                        new DayScheduleForDto("Friday", "11:00", "11:30")},
                "00:30");
        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.PATIENT);

        MvcResult result = mvc.perform(put("/api/doctors/123")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(updateScheduleDtoRequest)))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(),
                GlobalErrorHandler.MyError.class);

        ErrorModel errorModel = new ErrorModel("WRONG_USER_TYPE", "no field",
                "Only admins are allowed to change Doctors Schedule");
        assertEquals(errorModel, error.getAllErrors().get(0));
    }

    @Test
    public void testDeleteDoctorsFail() throws Exception {

        DeleteDoctorScheduleDtoRequest dtoRequest = new DeleteDoctorScheduleDtoRequest("01-01-2020");
        when(userService.getUserTypeBySession(anyString())).thenReturn(UserType.PATIENT);

        MvcResult result = mvc.perform(delete("/api/doctors/123")
                .contentType(MediaType.APPLICATION_JSON).cookie(new Cookie("JAVASESSIONID", "123"))
                .content(mapper.writeValueAsString(dtoRequest)))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        GlobalErrorHandler.MyError error = mapper.readValue(result.getResponse().getContentAsString(),
                GlobalErrorHandler.MyError.class);

        ErrorModel errorModel = new ErrorModel("WRONG_USER_TYPE", "no field",
                "Only admins are allowed to delete Doctors Schedule");
        assertEquals(errorModel, error.getAllErrors().get(0));
    }
}
