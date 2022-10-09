package top.cerbur.graduation.wechatapi.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * @author cerbur
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = SchoolIdValidator.class)
public @interface SchoolId {
    String message() default "{Day.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
