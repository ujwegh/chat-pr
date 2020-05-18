package ru.nik.chatpr.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.nik.chatpr.dto.UserRegistrationDto;
import ru.nik.chatpr.service.UserService;

import javax.validation.constraints.NotNull;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(@NotNull Class<?> aClass) {
        return UserRegistrationDto.class.equals(aClass);
    }

    @Override
    public void validate(@NotNull Object o, @NotNull Errors errors) {
        UserRegistrationDto user = (UserRegistrationDto) o;

        if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username");
        }
        if (userService.isExist(user.getUsername())) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }
        if (user.getPasswordRepeat().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("passwordRepeat", "Size.userForm.password");
        }
        if (!user.getPasswordRepeat().equals(user.getPassword())) {
            errors.rejectValue("password", "Repeat.userForm.password");
        }
    }
}
