package ru.nik.chatpr.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nik.chatpr.model.Room;
import ru.nik.chatpr.repository.RoomRepository;

import java.util.List;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
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
}
