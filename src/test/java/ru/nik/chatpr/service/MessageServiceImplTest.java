package ru.nik.chatpr.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.nik.chatpr.dto.UserRegistrationDto;
import ru.nik.chatpr.model.Message;
import ru.nik.chatpr.model.Room;
import ru.nik.chatpr.model.User;
import ru.nik.chatpr.repository.MessageRepository;
import ru.nik.chatpr.repository.RoomRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MessageServiceImplTest extends AbstractTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserService userService;

    private User user;
    private User admin;
    private Message expected;
    private String roomId;

    @BeforeEach
    void init() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("user@gmail.com");
        dto.setPassword("password");
        dto.setPasswordRepeat("password");
        dto.setName("user");
        user = userService.create(dto);
        dto.setUsername("admin@gmail.com");
        admin = userService.create(dto);
        Room room = new Room("room1", "some room", true, "user@gmail.com");
        room.setConnectedUsers(Arrays.asList(user, admin));
        Room savedRoom = roomRepository.save(room);
        roomId = savedRoom.getId();
        expected = new Message();
        expected.setFromUserEmail(user.getEmail());
        expected.setCreator(user.getEmail());
        expected.setRoomId(savedRoom.getId());
        expected.setText("some text");
        expected.setDateTime(LocalDateTime.now());
        expected = messageRepository.save(expected);
    }

    @AfterEach
    void clean() {
        messageRepository.deleteAll();
    }

    @Test
    void getAllMessagesByUserAndRoomId() {
        List<Message> actual = messageService.getAllMessagesByUserAndRoomId(user.getEmail(), roomId);
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(1, actual.size());
        assertNotNull(actual.get(0));
        assertEquals(expected.toString(), actual.get(0).toString());
    }

    @Test
    void sendPublicMessage() {
        Message message = new Message();
        message.setDateTime(LocalDateTime.now());
        message.setText("new text");
        message.setCreator(user.getEmail());
        message.setRoomId(roomId);
        message.setFromUserEmail(user.getEmail());
        messageService.sendPublicMessage(message);
        List<Message> actual = messageRepository.findAll();
        actual.sort(Comparator.comparing(Message::getFromUserEmail));
        assertNotNull(actual);
        assertEquals(3, actual.size());
    }

    @Test
    void sendPrivateMessage() {
        Message message = new Message();
        message.setDateTime(LocalDateTime.now());
        message.setText("new text");
        message.setCreator(user.getEmail());
        message.setRoomId(roomId);
        message.setFromUserEmail(user.getEmail());
        message.setToUserEmail(admin.getEmail());
        messageService.sendPrivateMessage(message);
        List<Message> actual = messageRepository.findAll();
        actual.sort(Comparator.comparing(Message::getFromUserEmail));
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(3, actual.size());
    }

    @Test
    void deleteAllMessagesByRoomId() {
        List<Message> existedMeaaseges = messageRepository.findAll();
        assertNotNull(existedMeaaseges);
        assertFalse(existedMeaaseges.isEmpty());
        messageService.deleteAllMessagesByRoomId(roomId);
        List<Message> actual = messageRepository.findAll();
        assertTrue(actual.isEmpty());
    }
}