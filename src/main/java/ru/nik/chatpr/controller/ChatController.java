package ru.nik.chatpr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.nik.chatpr.model.Room;
import ru.nik.chatpr.service.RoomService;

import java.util.List;

@Controller
public class ChatController {

    private final RoomService chatRoomService;

    @Autowired
    public ChatController(RoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @GetMapping(path = "/chats")
    public String getRooms(Model model) {
        List<Room> rooms = chatRoomService.findAll();
        model.addAttribute("rooms", rooms);
        return "chats";
    }
}
