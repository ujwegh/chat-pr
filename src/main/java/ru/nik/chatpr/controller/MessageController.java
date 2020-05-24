package ru.nik.chatpr.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import ru.nik.chatpr.model.Message;
import ru.nik.chatpr.service.MessageService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @SubscribeMapping("/messages/old.messages")
    public List<Message> listOldMessagesFromUserOnSubscribe(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        String chatRoomId = headerAccessor.getSessionAttributes().get("roomId").toString();
        return messageService.getAllMessagesByUserAndRoomId(principal.getName(), chatRoomId);
    }

    @MessageMapping("/messages/send.message")
    public void sendMessage(@Payload Message message, Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        log.debug("Send public message: {} from user: {}", message, principal.getName());
        String roomId = headerAccessor.getSessionAttributes().get("roomId").toString();
        message.setFromUserEmail(principal.getName());
        message.setRoomId(roomId);
        message.setDateTime(LocalDateTime.now());
        if (message.isPublic()) {
            messageService.sendPublicMessage(message);
        } else {
            messageService.sendPrivateMessage(message);
        }
    }
}
