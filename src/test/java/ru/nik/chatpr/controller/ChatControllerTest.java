package ru.nik.chatpr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.nik.chatpr.model.Room;
import ru.nik.chatpr.service.RoomService;
import ru.nik.chatpr.service.UserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ChatController.class)
class ChatControllerTest {
    @MockBean
    private RoomService roomService;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "user@gmail.com", roles = "USER")
    @Test
    void getRooms() throws Exception {
        Room room1 = new Room("room1", "some room 1", true, "user@gmail.com");
        room1.setId(UUID.randomUUID().toString());
        Room room2 = new Room("room2", "some room 2", false, "user@gmail.com");
        room2.setId(UUID.randomUUID().toString());
        Room room3 = new Room("room3", "some room 3", true, "user@gmail.com");
        room3.setId(UUID.randomUUID().toString());

        List<Room> rooms = Arrays.asList(room1, room2, room3);
        Mockito.when(roomService.findAll()).thenReturn(rooms);
        mockMvc.perform(get("/chats"))
                .andDo(print())
                .andExpect(status().isOk())
        .andExpect(model().attribute("rooms", rooms))
        .andExpect(model().attribute("principalEmail", "user@gmail.com"));

    }
}