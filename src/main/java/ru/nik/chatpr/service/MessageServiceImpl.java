package ru.nik.chatpr.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.nik.chatpr.model.Message;
import ru.nik.chatpr.model.Room;
import ru.nik.chatpr.repository.MessageRepository;

import java.util.List;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final RoomService roomService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, RoomService roomService, SimpMessagingTemplate simpMessagingTemplate) {
        this.messageRepository = messageRepository;
        this.roomService = roomService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public List<Message> getAllMessagesByUserAndRoomId(String email, String roomId) {
        log.debug("Find all messages for user: {} and roomId: {}", email, roomId);
        return messageRepository.findAllByEmailAndRoomId(email, roomId);
    }

    @Override
    public void createMessage(Message message) {
        log.debug("Create new message");
        String email = message.getFromUserEmail();
        if (message.isPublic()){
            Room room = roomService.findById(message.getRoomId());
            room.getConnectedUsers().forEach(user -> {
                message.setFromUserEmail(user.getEmail());
                messageRepository.save(message);
            });
        } else {
            //save for sender
            messageRepository.save(message);
            //save for receiver
            message.setFromUserEmail(message.getFromUserEmail());
            messageRepository.save(message);
        }
    }

    @Override
    public void sendPublicMessage(Message message) {
        log.debug("Send public message: {}", message);
        simpMessagingTemplate.convertAndSend("/topic/" + message.getRoomId() + ".public.messages", message);
        createMessage(message);
    }

    @Override
    public void sendPrivateMessage(Message message) {
        log.debug("Send private message: {}", message);
        simpMessagingTemplate.convertAndSendToUser(message.getFromUserEmail(), "/queue/" + message.getRoomId() + ".private.messages", message);
        simpMessagingTemplate.convertAndSendToUser(message.getToUserEmail(), "/queue/" + message.getRoomId() + ".private.messages", message);
        createMessage(message);
    }


}
