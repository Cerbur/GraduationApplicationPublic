package top.cerbur.graduation.wechatapi.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author cerbur
 */
public class SchoolIdValidator implements ConstraintValidator<SchoolId, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if ("".equals(s) || s == null) {
            return true;
        }
        String regex = "[0-4][1-2]{2}\\d{7}$";
        return Pattern.matches(regex, s);
    }
}
