package net.thumbtack.school.hospital.validator.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LoginValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
    String message() default "Incorrect {field}: '${validatedValue}'";

    String field() default " ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
