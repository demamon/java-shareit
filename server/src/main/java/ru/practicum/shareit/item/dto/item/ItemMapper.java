package ru.practicum.shareit.item.dto.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.dto.comment.CommentResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {
    public static ItemDto mapToItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

    public static ItemsDto mapToItemsDto(Item item, Optional<LocalDate> lastBooking, Optional<LocalDate> nextBooking,
                                         List<CommentResponseDto> comments) {
        ItemsDto dto = new ItemsDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        lastBooking.ifPresent(dto::setLastBooking);
        nextBooking.ifPresent(dto::setNextBooking);
        dto.setComments(comments);
        return dto;
    }

    public static Item mapToItemWithRequest(ItemWithRequestDto itemDto, User user, ItemRequest itemRequest) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                .itemRequest(itemRequest)
                .build();
    }

    public static void updateItemField(Item item, UpdateItemRequest request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }
        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }
        if (request.hasAvailable()) {
            item.setAvailable(request.getAvailable());
        }
    }

    public static ItemCommentsDto mapToItemCommentsDto(Item item, List<CommentResponseDto> comments,
                                                       BookingResponseDto lastBookings, BookingResponseDto nextBookings) {
        return ItemCommentsDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments)
                .lastBooking(lastBookings)
                .nextBooking(nextBookings)
                .build();
    }
}
