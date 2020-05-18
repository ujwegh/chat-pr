package ru.nik.chatpr.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.nik.chatpr.dto.UserRegistrationDto;
import ru.nik.chatpr.service.UserService;
import ru.nik.chatpr.util.UserValidator;

import javax.validation.Valid;

@Slf4j
@Controller
public class AuthController {

    private final UserService userService;

    private final UserValidator userValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @Autowired
    public AuthController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signin(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute("userDto") UserRegistrationDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "signup";
        userService.create(userDto);
        return "redirect:/";
    }
}
