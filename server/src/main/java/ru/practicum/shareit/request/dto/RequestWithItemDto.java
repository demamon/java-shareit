package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class RequestWithItemDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String description;
    LocalDateTime created;
    List<ItemsForRequestDto> items = new ArrayList<>();
}
