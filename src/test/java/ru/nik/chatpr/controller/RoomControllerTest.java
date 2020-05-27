package ru.nik.chatpr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.nik.chatpr.dto.RoomDto;
import ru.nik.chatpr.model.Room;
import ru.nik.chatpr.service.*;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RoomController.class)
class RoomControllerTest {

    @MockBean
    private RoomService roomService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "user@gmail.com", roles = "USER")
    @Test
    void createRoom() throws Exception {
        Room toSave = new Room("room1", "some room", true, "user@gmail.com");
        Room saved = new Room("room1", "some room", true, "user@gmail.com");
        saved.setId(UUID.randomUUID().toString());
        saved.setCreatorEmail("user@gmail.com");
        Mockito.when(roomService.create(toSave)).thenReturn(saved);
        RoomDto roomDto = new RoomDto();
        roomDto.setDescription("some room");
        roomDto.setName("room1");
        roomDto.setOpen(true);

        mockMvc.perform(post("/rooms/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId())))
                .andExpect(jsonPath("$.name", is(saved.getName())))
                .andExpect(jsonPath("$.description", is(saved.getDescription())))
                .andExpect(jsonPath("$.open", is(saved.getOpen())))
                .andExpect(jsonPath("$.creatorEmail", is(saved.getCreatorEmail())));
    }

    @Test
    void createRoom_redirect_to_login() throws Exception {
        RoomDto roomDto = new RoomDto();
        roomDto.setDescription("some room");
        roomDto.setName("room1");
        roomDto.setOpen(true);
        mockMvc.perform(post("/rooms/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomDto)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/"));
    }

    @WithMockUser(username = "user@gmail.com", roles = "USER")
    @Test
    void deleteRoom() throws Exception {
        String roomId = UUID.randomUUID().toString();
        Mockito.mock(MessageServiceImpl.class).deleteAllMessagesByRoomId(roomId);
        Mockito.mock(RoomServiceImpl.class).delete("user@gmail.com", roomId);
        mockMvc.perform(delete("/rooms/{roomId}", roomId))
                .andDo(print())
                .andExpect(status().isOk());
        Mockito.verify(roomService, Mockito.atLeastOnce()).delete("user@gmail.com", roomId);
        Mockito.verify(messageService, Mockito.atLeastOnce()).deleteAllMessagesByRoomId(roomId);
    }

    @WithMockUser(username = "user@gmail.com", roles = "USER")
    @Test
    void joinRoom() throws Exception {
        String roomId = UUID.randomUUID().toString();
        Room saved = new Room("room1", "some room", true, "user@gmail.com");
        saved.setId(roomId);
        saved.setCreatorEmail("user@gmail.com");
        Mockito.when(roomService.joinRoom("user@gmail.com", roomId)).thenReturn(saved);
        mockMvc.perform(get("/rooms/{roomId}", roomId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("room", saved));
        Mockito.verify(roomService, Mockito.atLeastOnce()).joinRoom("user@gmail.com", roomId);
    }
}