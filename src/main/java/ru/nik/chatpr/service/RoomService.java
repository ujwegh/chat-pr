package ru.nik.chatpr.service;

import ru.nik.chatpr.model.Room;

import java.util.List;

public interface RoomService {
    Room findById(String roomId);
    Room create(Room room);
    void delete(String email, String roomId);
    List<Room> findAll();
    Room joinRoom(String email, String roomId);
    Room leaveRoom(String email, String roomId);
}
