package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.dao.dao.AdminDao;
import net.thumbtack.school.hospital.dao.dao.DoctorDao;
import net.thumbtack.school.hospital.dao.dao.PatientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/debug/clear")
public class DebugClearEndPoint {

    private AdminDao adminDao;
    private PatientDao patientDao;
    private DoctorDao doctorDao;

    @Autowired
    public DebugClearEndPoint(AdminDao adminDao, PatientDao patientDao, DoctorDao doctorDao) {
        this.adminDao = adminDao;
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void clear() {

        doctorDao.deleteAll();
        patientDao.deleteAll();
        adminDao.deleteAllExceptOne();
    }

}
