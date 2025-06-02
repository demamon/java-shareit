package ru.practicum.shareit.item.dto.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.dto.comment.CommentResponseDto;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class ItemsDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String name;
    String description;
    Boolean available;
    LocalDate nextBooking;
    LocalDate lastBooking;
    List<CommentResponseDto> comments;
}
