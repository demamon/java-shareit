package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemRequestMapper {
    public static ItemRequest mapToRequest(User user, String description) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(description);
        itemRequest.setAuthor(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    public static ItemRequestDto mapToRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }

    public static RequestWithItemDto mapToRequestWithItem(ItemRequest itemRequest, List<Item> items) {
        RequestWithItemDto request = new RequestWithItemDto();
        request.setId(itemRequest.getId());
        request.setDescription(itemRequest.getDescription());
        request.setCreated(itemRequest.getCreated());
        List<ItemsForRequestDto> itemsMap = items.stream()
                .map(item -> {
                    ItemsForRequestDto items1 = new ItemsForRequestDto();
                    items1.setName(item.getName());
                    items1.setId(item.getId());
                    items1.setIdOwner(item.getOwner().getId());
                    return items1;
                })
                .toList();
        request.setItems(itemsMap);
        return request;
    }
}
