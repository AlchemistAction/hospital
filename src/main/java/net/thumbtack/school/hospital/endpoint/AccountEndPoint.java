package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dto.request.LoginDtoRequest;
import net.thumbtack.school.hospital.dto.response.ReturnAdminDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnPatientDtoResponse;
import net.thumbtack.school.hospital.dto.response.ReturnUserDtoResponse;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.model.exception.HospitalException;
import net.thumbtack.school.hospital.mybatis.dao.AdminDao;
import net.thumbtack.school.hospital.mybatis.dao.DoctorDao;
import net.thumbtack.school.hospital.mybatis.dao.PatientDao;
import net.thumbtack.school.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/account")
public class AccountEndPoint {

    private UserService userService;
    private PatientDao patientDao;
    private DoctorDao doctorDao;
    private AdminDao adminDao;

    @Autowired
    public AccountEndPoint(UserService userService, PatientDao patientDao, DoctorDao doctorDao, AdminDao adminDao) {
        this.userService = userService;
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
        this.adminDao = adminDao;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnUserDtoResponse login(@CookieValue(value = "userId", defaultValue = "-1") int id,
                                       @CookieValue(value = "userType", defaultValue = "user") String userType) throws HospitalException {

        return userService.getInfo(id, userType);
    }
}
