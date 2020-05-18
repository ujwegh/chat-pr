package ru.nik.chatpr.service;

import ru.nik.chatpr.model.Room;

import java.util.List;

public interface RoomService {
    Room findById(String roomId);
    Room create(Room room);
    List<Room> findAll();

}
