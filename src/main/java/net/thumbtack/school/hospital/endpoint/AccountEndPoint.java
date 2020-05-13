package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.response.ReturnUserDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/account")
public class AccountEndPoint {

    private UserService userService;

    @Autowired
    public AccountEndPoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnUserDtoResponse getInfo(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID) throws HospitalException {

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        int id = userService.getIdBySession(JAVASESSIONID);

        return userService.getInfo(id, userType);
    }
}
