package net.thumbtack.school.hospital;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private int max_name_length;
    private int min_password_length;

    public int getMax_name_length() {
        return max_name_length;
    }

    public int getMin_password_length() {
        return min_password_length;
    }


}
