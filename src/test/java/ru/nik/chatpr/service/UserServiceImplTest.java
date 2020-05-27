package ru.nik.chatpr.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.nik.chatpr.dto.UserRegistrationDto;
import ru.nik.chatpr.exceptions.NotFoundException;
import ru.nik.chatpr.model.User;
import ru.nik.chatpr.model.enums.Gender;
import ru.nik.chatpr.model.enums.Role;
import ru.nik.chatpr.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceImplTest extends AbstractTest{

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository repository;

    private User expectedUser;

    @BeforeEach
    void init() {
        expectedUser = new User("user@gmail.com", "password", Role.ROLE_USER);
        expectedUser.setFirstName("user");
        expectedUser.setGender(Gender.MALE);
        expectedUser.setLastName("user");
        expectedUser.setPhone("8-800-555-35-35");
        expectedUser = repository.save(expectedUser);
    }

    @Test
    void create() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setName("admin");
        dto.setPassword("password");
        dto.setPasswordRepeat("password");
        dto.setUsername("admin@gmail.com");
        User actual = userService.create(dto);
        assertNotNull(actual);
        assertEquals(dto.getUsername(), actual.getEmail());
        assertEquals(dto.getName(), actual.getFirstName());
    }

    @Test
    void delete() {
        userService.delete(expectedUser.getId());
        User actualUser = repository.findById(expectedUser.getId()).orElse(null);
        assertNull(actualUser);
    }

    @Test
    void getById() {
        User actualUser = userService.getById(expectedUser.getId());
        assertNotNull(actualUser);
        assertEquals(expectedUser.toString(), actualUser.toString());
    }

    @Test
    void getByEmail() {
        User actualUser = userService.getByEmail(expectedUser.getEmail());
        assertNotNull(actualUser);
        assertEquals(expectedUser.toString(), actualUser.toString());
    }

    @Test
    void getAll() {
        List<User> actualUsers = userService.getAll();
        assertNotNull(actualUsers);
        assertFalse(actualUsers.isEmpty());
        assertEquals(1, actualUsers.size());
        assertEquals(expectedUser, actualUsers.get(0));
    }

    @Test
    void update() {
        expectedUser.setFirstName("new name");
        userService.update(expectedUser);
    }

    @Test
    void disable() {
        expectedUser.setEnabled(false);
        userService.enable(expectedUser.getId(), false);
        User actualUser = repository.findById(expectedUser.getId()).orElse(null);
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void isExist() {
        boolean actualIs = userService.isExist(expectedUser.getEmail());
        assertTrue(actualIs);
    }
}