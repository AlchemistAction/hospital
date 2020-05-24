package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.ApplicationProperties;
import net.thumbtack.school.hospital.dao.mybatis.daoimpl.AdminDaoImpl;
import net.thumbtack.school.hospital.dto.response.SettingsDtoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class SettingsEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Autowired
    private ApplicationProperties appl;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SettingsDtoResponse getSettings(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID) {
        LOGGER.info("SettingsEndPoint getSettings");
        return new SettingsDtoResponse(appl.getMaxNameLength(), appl.getMinPasswordLength());
    }

}
