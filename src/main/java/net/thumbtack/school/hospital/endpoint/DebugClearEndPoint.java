package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dao.dao.AdminDao;
import net.thumbtack.school.hospital.dao.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.dao.PatientDao;
import net.thumbtack.school.hospital.dao.dao.UserDao;
import net.thumbtack.school.hospital.dao.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/debug/clear")
public class DebugClearEndPoint {

    private final AdminDao adminDao;
    private final PatientDao patientDao;
    private final DoctorDao doctorDao;
    private final UserDao userDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Autowired
    public DebugClearEndPoint(AdminDao adminDao, PatientDao patientDao, DoctorDao doctorDao, UserDao userDao) {
        this.adminDao = adminDao;
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
        this.userDao = userDao;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void clear() {
        LOGGER.info("DebugClearEndPoint clear database");
        doctorDao.deleteAll();
        patientDao.deleteAll();
        adminDao.deleteAllExceptOne();
        userDao.deleteAllSessions();
    }
}
