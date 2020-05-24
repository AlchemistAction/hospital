package net.thumbtack.school.hospital.validator.annotations;

import net.thumbtack.school.hospital.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PatronymicValidator implements ConstraintValidator<Patronymic, String> {
    @Autowired
    private ApplicationProperties ap;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        return (Pattern.matches("[а-яА-Я]+", s) && (s.length() < ap.getMaxNameLength()));
    }
}
