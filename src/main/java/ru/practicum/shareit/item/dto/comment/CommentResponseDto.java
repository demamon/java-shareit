package ru.practicum.shareit.item.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentResponseDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String text;
    Item item;
    String authorName;
    LocalDateTime created;
}
