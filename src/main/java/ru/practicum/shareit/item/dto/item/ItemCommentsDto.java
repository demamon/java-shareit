package ru.practicum.shareit.item.dto.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.dto.comment.CommentResponseDto;

import java.util.List;

@Getter
@Setter
@ToString
public class ItemCommentsDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String name;
    String description;
    Boolean available;
    List<CommentResponseDto> comments;
    BookingResponseDto lastBookings;
    BookingResponseDto nextBookings;
}
