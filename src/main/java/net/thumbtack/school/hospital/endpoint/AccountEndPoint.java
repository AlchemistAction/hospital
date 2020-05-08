package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.response.ReturnUserDtoResponse;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


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
            @CookieValue(value = "userId", defaultValue = "-1") int id,
            @CookieValue(value = "userType", defaultValue = "user") String userType) throws HospitalException {

        return userService.getInfo(id, userType);
    }
}
