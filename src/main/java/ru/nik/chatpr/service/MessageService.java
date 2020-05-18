package ru.nik.chatpr.service;

import ru.nik.chatpr.model.Message;

import java.util.List;

public interface MessageService {
    List<Message> getAllMessagesByUserAndRoomId(String email, String roomId);
    void createMessage(Message message);
    void sendPublicMessage(Message message);
    void sendPrivateMessage(Message message);

}
