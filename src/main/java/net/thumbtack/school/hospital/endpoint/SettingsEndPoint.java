package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.ApplicationProperties;
import net.thumbtack.school.hospital.dto.response.SettingsDtoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class SettingsEndPoint {

    @Autowired
    private ApplicationProperties appl;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SettingsDtoResponse getSettings(
            @CookieValue(value = "JAVASESSIONID", defaultValue = "-1") String JAVASESSIONID) {

        return new SettingsDtoResponse(appl.getMaxNameLength(), appl.getMinPasswordLength());
    }

}
