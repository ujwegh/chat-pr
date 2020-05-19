package ru.nik.chatpr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.nik.chatpr.exceptions.NotFoundException;

@Service
public final class WebSocketEventService {

    private final RoomService roomService;

    @Autowired
    public WebSocketEventService(RoomService roomService) {
        this.roomService = roomService;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String roomId = headers.getFirstNativeHeader("roomId");
        String userEmail = event.getUser().getName();
        if (roomId == null) throw new NotFoundException("RoomId header not found.");
        if (userEmail == null) throw new NotFoundException("User email not found.");
        headers.getSessionAttributes().put("roomId", roomId);
        roomService.joinRoom(userEmail, roomId);
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String roomId = headers.getSessionAttributes().get("roomId").toString();
        String userEmail = event.getUser().getName();
        if (userEmail == null) throw new NotFoundException("User email not found.");
        roomService.leaveRoom(userEmail, roomId);
    }

}
