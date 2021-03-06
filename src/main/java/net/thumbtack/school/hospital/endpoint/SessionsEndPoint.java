package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dao.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.model.EmptyJsonResponse;
import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnUserDtoResponse;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import net.thumbtack.school.hospital.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@RestController
@RequestMapping("/api/sessions")
public class SessionsEndPoint {

    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Autowired
    public SessionsEndPoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnUserDtoResponse login(@RequestBody LoginDtoRequest loginDtoRequest,
                                       HttpServletResponse response) throws HospitalException {
        LOGGER.info("SessionsEndPoint login {} ", loginDtoRequest);
        String uuid = UUID.randomUUID().toString();
        ReturnUserDtoResponse dtoResponse = userService.login(loginDtoRequest, uuid);

        Cookie cookie = new Cookie("JAVASESSIONID", uuid);
        response.addCookie(cookie);
        return dtoResponse;
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity logout(@CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
                       HttpServletResponse response) {
        LOGGER.info("SessionsEndPoint logout");
        Cookie cookie = new Cookie("JAVASESSIONID", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        userService.logout(JAVASESSIONID);
        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
    }
}
