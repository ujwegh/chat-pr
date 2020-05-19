package ru.nik.chatpr.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class RoomDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotEmpty
    @NotNull
    private Boolean open;
}
