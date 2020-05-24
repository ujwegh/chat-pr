package ru.nik.chatpr.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
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
    @PrimaryKeyColumn(name = "fromUserEmail", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String fromUserEmail;
    private String toUserEmail;
    @PrimaryKeyColumn(name = "roomId", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String roomId;
    @PrimaryKeyColumn(name = "dateTime", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private LocalDateTime dateTime;
    @PrimaryKeyColumn(name = "creator", ordinal = 3, type = PrimaryKeyType.PARTITIONED)
    private String creator;

    public boolean isPublic() {
        return StringUtils.isEmpty(this.toUserEmail);
    }

}
