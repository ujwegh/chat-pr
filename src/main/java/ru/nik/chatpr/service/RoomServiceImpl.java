package ru.nik.chatpr.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.nik.chatpr.model.Room;
import ru.nik.chatpr.model.User;
import ru.nik.chatpr.repository.RoomRepository;

import java.util.List;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, UserService userService, SimpMessagingTemplate simpMessagingTemplate) {
        this.roomRepository = roomRepository;
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public Room findById(String roomId) {
        log.debug("Find room by id: {}", roomId);
        return roomRepository.findById(roomId).orElse(null);
    }

    @Override
    public Room create(Room room) {
        log.debug("Create new room: {}", room);
        return roomRepository.save(room);
    }

    @Override
    public List<Room> findAll() {
        log.debug("Find all rooms");
        return roomRepository.findAll();
    }

    @Override
    public Room joinRoom(String email, String roomId) {
        log.debug("Join user: {} to room with id: {}", email, roomId);
        Room room = findById(roomId);
        User user = userService.getByEmail(email);
        room.getConnectedUsers().add(user);
        roomRepository.save(room);
        updateConnectedUsersViaWebSocket(room);
        return room;
    }

    @Override
    public Room leaveRoom(String email, String roomId) {
        log.debug("Leave user: {} from room with id: {}", email, roomId);
        Room room = findById(roomId);
        room.getConnectedUsers().removeIf(user -> user.getEmail().equals(email));
        roomRepository.save(room);
        updateConnectedUsersViaWebSocket(room);
        return room;
    }

    private void updateConnectedUsersViaWebSocket(Room room) {
        simpMessagingTemplate.convertAndSend("/topic/" + room.getId() + ".connected.users", room.getConnectedUsers());
    }
}
