package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnUserDtoResponse;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@RestController
@RequestMapping("/api/sessions")
public class SessionsEndPoint {

    private UserService userService;

    @Autowired
    public SessionsEndPoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnUserDtoResponse login(@RequestBody LoginDtoRequest loginDtoRequest,
                                       HttpServletResponse response) throws HospitalException {

        String uuid = UUID.randomUUID().toString();

        ReturnUserDtoResponse dtoResponse = userService.login(loginDtoRequest, uuid);

        if (dtoResponse instanceof ReturnPatientDtoResponse) {
            createCookie(response, uuid);
        }
        if (dtoResponse instanceof ReturnAdminDtoResponse) {
            createCookie(response, uuid);
        }
        if (dtoResponse instanceof ReturnDoctorDtoResponse) {
            createCookie(response, uuid);
        }

        return dtoResponse;
    }

    private void createCookie(HttpServletResponse response, String uuid) {
        Cookie cookie = new Cookie("JAVASESSIONID", uuid);
        response.addCookie(cookie);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void logout(@CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
                       HttpServletResponse response) {

        Cookie cookie = new Cookie("JAVASESSIONID", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        userService.logout(JAVASESSIONID);
    }

}
