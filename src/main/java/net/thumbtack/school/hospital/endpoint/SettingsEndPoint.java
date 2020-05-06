package net.thumbtack.school.hospital.endpoint;

import net.thumbtack.school.hospital.ApplicationProperties;
import net.thumbtack.school.hospital.dto.response.SettingsDtoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
public class SettingsEndPoint {

    @Autowired
    private ApplicationProperties appl;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SettingsDtoResponse getSettings(
            @CookieValue(value = "userId", defaultValue = "-1") int id,
            @CookieValue(value = "userType", defaultValue = "user") String userType) {

        return new SettingsDtoResponse(appl.getMaxNameLength(), appl.getMinPasswordLength());
    }

}
