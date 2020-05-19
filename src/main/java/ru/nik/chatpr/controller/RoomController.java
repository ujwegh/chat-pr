package ru.nik.chatpr.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.nik.chatpr.dto.RoomDto;
import ru.nik.chatpr.model.Room;
import ru.nik.chatpr.model.User;
import ru.nik.chatpr.service.MessageService;
import ru.nik.chatpr.service.RoomService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("rooms")
public class RoomController {
    private final RoomService roomService;
    private final MessageService messageService;

    @Autowired
    public RoomController(RoomService roomService, MessageService messageService) {
        this.roomService = roomService;
        this.messageService = messageService;
    }

    @Secured("ROLE_USER")
    @PostMapping("/room")
    @ResponseBody
    public Room createRoom(@RequestBody @Valid RoomDto roomDto) {
        log.info("Create new Room: {}", roomDto);
        Room room = new Room(roomDto.getName(), roomDto.getDescription(), roomDto.getOpen());
        return roomService.create(room);
    }

    @Secured("ROLE_USER")
    @GetMapping("/room/{roomId}")
    public String joinRoom(@PathVariable("roomId") String roomId, Model model){
        model.addAttribute("room", roomService.findById(roomId));
        return "room";
    }

    @SubscribeMapping("/users")
    public List<User> listChatRoomConnectedUsersOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = headerAccessor.getSessionAttributes().get("roomId").toString();
        return roomService.findById(chatRoomId).getConnectedUsers();
    }


}
