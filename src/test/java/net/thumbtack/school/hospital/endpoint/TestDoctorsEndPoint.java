package net.thumbtack.school.hospital.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.hospital.dao.dao.DoctorDao;
import net.thumbtack.school.hospital.dto.internal.WeekSchedule;
import net.thumbtack.school.hospital.dto.request.RegisterDoctorDtoRequest;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.service.DoctorService;
import net.thumbtack.school.hospital.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
                "рома", "patronymic", "хирург", "100", "doctorLogin",
                "doctorPassword", "13-04-2020", "16-04-2020",
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
}
