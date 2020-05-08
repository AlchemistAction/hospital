package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnUserDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


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

        ReturnUserDtoResponse dtoResponse = userService.login(loginDtoRequest);

        if (dtoResponse instanceof ReturnPatientDtoResponse) {
            Cookie cookie1 = new Cookie("userId", String.valueOf(dtoResponse.getId()));
            Cookie cookie2 = new Cookie("userType", String.valueOf(UserType.PATIENT));
            response.addCookie(cookie1);
            response.addCookie(cookie2);
        }
        if (dtoResponse instanceof ReturnAdminDtoResponse) {
            Cookie cookie1 = new Cookie("userId", String.valueOf(dtoResponse.getId()));
            Cookie cookie2 = new Cookie("userType", String.valueOf(UserType.ADMIN));
            response.addCookie(cookie1);
            response.addCookie(cookie2);
        }
        if (dtoResponse instanceof ReturnDoctorDtoResponse) {
            Cookie cookie1 = new Cookie("userId", String.valueOf(dtoResponse.getId()));
            Cookie cookie2 = new Cookie("userType", String.valueOf(UserType.DOCTOR));
            response.addCookie(cookie1);
            response.addCookie(cookie2);
        }

        return dtoResponse;
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void logout(
            @CookieValue(value = "userId", defaultValue = "-1") int id,
            @CookieValue(value = "userType", defaultValue = "user") String userType, HttpServletResponse response) {

        Cookie cookie1 = new Cookie("userId", String.valueOf(id));
        cookie1.setMaxAge(0);
        Cookie cookie2 = new Cookie("userType", userType);
        cookie2.setMaxAge(0);

        response.addCookie(cookie1);
        response.addCookie(cookie2);
    }

}
