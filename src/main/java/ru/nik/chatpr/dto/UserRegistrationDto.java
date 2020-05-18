package ru.nik.chatpr.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@ToString
@Getter
@Setter
public class UserRegistrationDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotEmpty
    private String name;
    @Email
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String passwordRepeat;
}
