package ru.nik.chatpr.service;

import ru.nik.chatpr.model.Message;

import java.util.List;

public interface MessageService {
    List<Message> getAllMessagesByUserAndRoomId(String email, String roomId);
    void sendPublicMessage(Message message);
    void sendPrivateMessage(Message message);
    void deleteAllMessagesByRoomId(String roomId);
}
