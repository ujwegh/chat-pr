package ru.nik.chatpr.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@RedisHash("room")
@Getter
@Setter
@EqualsAndHashCode
public class Room {
    @Id
    private String id;
    private String name;
    private String description;
    private Boolean open;
    private String creatorEmail;
    private List<User>  connectedUsers = new ArrayList<>();

    public Room(String name, String description, Boolean open, String creatorEmail) {
        this.name = name;
        this.description = description;
        this.open = open;
        this.creatorEmail = creatorEmail;
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", open=" + open +
                ", creatorEmail='" + creatorEmail + '\'' +
                '}';
    }
}
