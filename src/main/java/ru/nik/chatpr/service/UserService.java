package ru.nik.chatpr.service;

import ru.nik.chatpr.dto.UserRegistrationDto;
import ru.nik.chatpr.model.User;

import java.util.List;

public interface UserService {
    User create(UserRegistrationDto user);
    void delete(long id);
    User getById(long id);
    User getByEmail(String email);
    List<User> getAll();
    void update(User user);
    void enable(long id, boolean enabled);
    boolean isExist(String email);
}
