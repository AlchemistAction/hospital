package net.thumbtack.school.hospital.validator;


import net.thumbtack.school.hospital.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NameValidator implements ConstraintValidator<Name, String> {

    @Autowired
    private ApplicationProperties ap;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return (s != null && !s.matches("[0-9]+") && !Pattern.matches("[a-zA-Z]+", s)
                && (s.length() > ap.getMax_name_length()));
    }
}

