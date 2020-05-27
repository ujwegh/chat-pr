package ru.nik.chatpr.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.nik.chatpr.dto.UserRegistrationDto;
import ru.nik.chatpr.model.Room;
import ru.nik.chatpr.model.User;
import ru.nik.chatpr.repository.RoomRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class RoomServiceImplTest extends AbstractTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserService userService;

    private Room expected;
    private User user;

    @BeforeEach
    void init() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("user@gmail.com");
        dto.setPassword("password");
        dto.setPasswordRepeat("password");
        dto.setName("user");
        user = userService.create(dto);
        expected = new Room("room1", "some room", true, "user@gmail.com");
        expected.setConnectedUsers(Collections.singletonList(user));
        expected = roomRepository.save(expected);
    }

    @AfterEach
    void clean() {
        roomRepository.deleteAll();
    }

    @Test
    void findById() {
        Room actual = roomService.findById(expected.getId());
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void create() {
        Room expected = new Room("room 2", "some room 2", false, "user@gmail.com");
        Room actual = roomService.create(expected);
        assertNotNull(actual);
        assertNotNull(actual.getId());
    }

    @Test
    void delete() {
        roomService.delete(user.getEmail(), expected.getId());
        Room actual = roomRepository.findById(expected.getId()).orElse(null);
        assertNull(actual);
    }

    @Test
    void findAll() {
        List<Room> actual = roomService.findAll();
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(1, actual.size());
    }

    @Test
    void joinRoom() {
        expected.setConnectedUsers(Collections.singletonList(user));
        Room actual = roomService.joinRoom(user.getEmail(), expected.getId());
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
        assertFalse(actual.getConnectedUsers().isEmpty());
        assertEquals(1, actual.getConnectedUsers().size());
        assertEquals(user.toString(), actual.getConnectedUsers().get(0).toString());
    }

    @Test
    void leaveRoom() {
        Room actual = roomService.leaveRoom(user.getEmail(), expected.getId());
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
        assertTrue(actual.getConnectedUsers().isEmpty());
    }
}