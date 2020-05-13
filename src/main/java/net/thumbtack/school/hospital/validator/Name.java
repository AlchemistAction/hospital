package net.thumbtack.school.hospital.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    String message() default "{field}: '${validatedValue}'";

    String field() default " ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
