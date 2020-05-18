package ru.nik.chatpr.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.nik.chatpr.model.Room;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, String> {
    List<Room> findAll();
}
