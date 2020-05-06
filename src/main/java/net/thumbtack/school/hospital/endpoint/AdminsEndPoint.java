package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.ApplicationProperties;
import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.mybatis.dao.AdminDao;
import net.thumbtack.school.hospital.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.service.AdminService;
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

    private AdminDao adminDao;

    @Autowired
    public AdminsEndPoint(AdminService adminService, AdminDao adminDao) {
        this.adminService = adminService;
        this.adminDao = adminDao;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnAdminDtoResponse registerAdmin(@CookieValue(value = "userId", defaultValue = "-1") int id,
                                                @CookieValue(value = "userType", defaultValue = "user") String userType,
                                                @Valid @RequestBody RegisterAdminDtoRequest registerAdminDtoRequest) {

        if (userType.equals(String.valueOf(UserType.ADMIN))) {
            return adminService.registerAdmin(registerAdminDtoRequest);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Only admins are allowed to register other admins");
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnAdminDtoResponse updateAdmin(
            @CookieValue(value = "userId", defaultValue = "-1") int id,
            @CookieValue(value = "userType", defaultValue = "user") String userType,
            @Valid @RequestBody UpdateAdminDtoRequest updateAdminDtoRequest) throws HospitalException {

        return adminService.updateAdmin(updateAdminDtoRequest, id);
    }

}
