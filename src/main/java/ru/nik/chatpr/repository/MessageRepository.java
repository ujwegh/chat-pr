package ru.nik.chatpr.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import ru.nik.chatpr.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends CassandraRepository<Message, String> {
    List<Message> findAllByFromUserEmailAndRoomId(String email, String roomId);
    void deleteAllByRoomId(String roomId);
}
