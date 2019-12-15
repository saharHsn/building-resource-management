package tech.builtrix.validations;

import com.google.common.base.Joiner;
import tech.builtrix.dtos.emailToken.RegisterUserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    PasswordMatches conAnn;

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        this.conAnn = constraintAnnotation;
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final RegisterUserDto user = (RegisterUserDto) obj;
        boolean valid = user.getPassword().equals(user.getConfirmPassword());
        if (valid) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(Joiner.on(",").join(Arrays.asList(conAnn.message())))
                .addConstraintViolation();
        return false;
    }

}
