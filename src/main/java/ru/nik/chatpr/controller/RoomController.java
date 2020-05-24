package ru.nik.chatpr.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nik.chatpr.dto.RoomDto;
import ru.nik.chatpr.model.Room;
import ru.nik.chatpr.model.User;
import ru.nik.chatpr.service.MessageService;
import ru.nik.chatpr.service.RoomService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
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

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @PostMapping("/")
    @ResponseBody
    public Room createRoom(@RequestBody @Valid RoomDto roomDto, Principal principal) {
        log.debug("Create new Room: {} by user: {}", roomDto, principal.getName());
        Room room = new Room(roomDto.getName(), roomDto.getDescription(), roomDto.getOpen(), principal.getName());
        return roomService.create(room);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @DeleteMapping("/{roomId}")
    public String deleteRoom(@PathVariable("roomId") @NotEmpty String roomId, Principal principal) {
        log.debug("Delete room with id: {} by user: {}", roomId, principal.getName());
        roomService.delete(principal.getName(), roomId);
        messageService.deleteAllMessagesByRoomId(roomId);
        return "chats";
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping("/{roomId}")
    public String joinRoom(@PathVariable("roomId") @NotEmpty String roomId, Model model, Principal principal){
        log.debug("Join user: {} to room with id: {}",principal.getName(), roomId);
        Room room = roomService.joinRoom(principal.getName(), roomId);
        model.addAttribute("room", room);
        return "room";
    }

    @SubscribeMapping("/users")
    public List<User> listChatRoomConnectedUsersOnSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = headerAccessor.getSessionAttributes().get("roomId").toString();
        return roomService.findById(chatRoomId).getConnectedUsers();
    }


}
