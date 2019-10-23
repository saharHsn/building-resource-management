package tech.builtrix.validation;

import tech.builtrix.dto.emailToken.RegisterUserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final RegisterUserDto user = (RegisterUserDto) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }

}
