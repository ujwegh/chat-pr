package ru.nik.chatpr.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.nik.chatpr.model.Room;
import ru.nik.chatpr.service.RoomService;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
public class ChatController {

    private final RoomService chatRoomService;

    @Autowired
    public ChatController(RoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(path = "/chats")
    public String getRooms(Model model, Principal principal) {
        log.debug("Get all chat rooms");
        List<Room> rooms = chatRoomService.findAll();
        model.addAttribute("rooms", rooms);
        model.addAttribute("principalEmail", principal.getName());
        return "chats";
    }
}
