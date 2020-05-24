package net.thumbtack.school.hospital.validator.annotations;

import net.thumbtack.school.hospital.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    @Autowired
    private ApplicationProperties ap;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return ((s != null) && (s.length() < ap.getMaxNameLength()) && (s.length() > ap.getMinPasswordLength()));
    }
}
