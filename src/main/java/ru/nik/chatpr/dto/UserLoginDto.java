package ru.nik.chatpr.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ToString
@Getter
@Setter
public class UserLoginDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
