package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Collection;

public interface ItemService {
    ItemDto create(long userId, ItemDto itemDto);

    ItemDto update(long userId, long itemId, UpdateItemRequest request);

    ItemDto findItemId(long itemId);

    Collection<ItemDto> findItemUserId(long userId);

    Collection<ItemDto> findItemText(String text);
}
