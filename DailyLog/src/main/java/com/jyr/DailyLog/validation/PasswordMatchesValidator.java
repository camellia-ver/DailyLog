package com.jyr.DailyLog.validation;

import com.jyr.DailyLog.dto.UserSignupRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserSignupRequestDto> {
    @Override
    public boolean isValid(UserSignupRequestDto value, ConstraintValidatorContext context) {
        if (value.getPassword() == null || value.getConfirmPassword() == null){
            return false;
        }
        boolean matches = value.getPassword().equals(value.getConfirmPassword());
        if (!matches){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("비밀번호가 일치하지 않습니다.")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return matches;
    }
}
