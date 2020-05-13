package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.service.AdminService;
import net.thumbtack.school.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admins")
public class AdminsEndPoint {

    private AdminService adminService;
    private UserService userService;

    @Autowired
    public AdminsEndPoint(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnAdminDtoResponse registerAdmin(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @Valid @RequestBody RegisterAdminDtoRequest registerAdminDtoRequest) {

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType.equals(UserType.ADMIN)) {
            return adminService.registerAdmin(registerAdminDtoRequest);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Only admins are allowed to register other admins");
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnAdminDtoResponse updateAdmin(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @Valid @RequestBody UpdateAdminDtoRequest updateAdminDtoRequest) throws HospitalException {

        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType.equals(UserType.ADMIN)) {
            int id = userService.getIdBySession(JAVASESSIONID);
            return adminService.updateAdmin(updateAdminDtoRequest, id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "You are not allowed to do this operation");
        }
    }

}
