package ru.nik.chatpr.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@Table("messages")
@EqualsAndHashCode
@ToString
public class Message {

    private String text;
    private LocalDateTime dateTime;
    private String roomId;
    private String fromUserEmail;
    private String toUserEmail;

    public boolean isPublic() {
        return StringUtils.isEmpty(this.toUserEmail);
    }

}
