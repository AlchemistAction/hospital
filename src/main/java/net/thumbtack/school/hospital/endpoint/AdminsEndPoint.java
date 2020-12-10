package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dao.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.dto.request.RegisterAdminDtoRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.validator.ErrorModel;
import net.thumbtack.school.hospital.validator.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import net.thumbtack.school.hospital.service.AdminService;
import net.thumbtack.school.hospital.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admins")
public class AdminsEndPoint {

    private final AdminService adminService;
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Autowired
    public AdminsEndPoint(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnAdminDtoResponse registerAdmin(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @Valid @RequestBody RegisterAdminDtoRequest registerAdminDtoRequest) throws HospitalException {
        LOGGER.info("AdminsEndPoint register Admin {} ", registerAdminDtoRequest);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType != null && userType.equals(UserType.ADMIN)) {
            return adminService.registerAdmin(registerAdminDtoRequest);
        } else {
            LOGGER.debug("AdminsEndPoint cant register Admin. wrong userType {} ", userType);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                    "Only admins are allowed to register other admins"));
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnAdminDtoResponse updateAdmin(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID,
            @Valid @RequestBody UpdateAdminDtoRequest updateAdminDtoRequest) throws HospitalException {
        LOGGER.info("AdminsEndPoint update Admin {} ", updateAdminDtoRequest);
        UserType userType = userService.getUserTypeBySession(JAVASESSIONID);
        if (userType != null && userType.equals(UserType.ADMIN)) {
            int id = userService.getIdBySession(JAVASESSIONID);
            return adminService.updateAdmin(updateAdminDtoRequest, id);
        } else {
            LOGGER.info("AdminsEndPoint cant update Admin. wrong userType  {} ", userType);
            throw new HospitalException(new ErrorModel(HospitalErrorCode.WRONG_USER_TYPE, "no field",
                    "Admin can update his own info only"));
        }
    }
}
