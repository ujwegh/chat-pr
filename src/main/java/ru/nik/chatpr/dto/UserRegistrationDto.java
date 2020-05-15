package ru.nik.chatpr.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class UserRegistrationDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String email;
    private String password;
}
