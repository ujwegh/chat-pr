package ru.nik.chatpr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.nik.chatpr.dto.UserRegistrationDto;

import javax.validation.Valid;

@Controller
public class AuthController {

    @RequestMapping("/")
    public String login() {
        return "login";
    }

    public String register(@Valid UserRegistrationDto dto){

    }
}
