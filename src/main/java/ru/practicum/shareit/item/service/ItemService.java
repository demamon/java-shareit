package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.item.ItemCommentsDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemsDto;
import ru.practicum.shareit.item.dto.item.UpdateItemRequest;

import java.util.Collection;

public interface ItemService {
    ItemDto create(long userId, ItemDto itemDto);

    ItemDto update(long userId, long itemId, UpdateItemRequest request);

    ItemCommentsDto findItemId(long itemId, Long userId);

    Collection<ItemsDto> findItemUserId(long userId);

    Collection<ItemDto> findItemText(String text);

    CommentResponseDto addComment(Long itemId, Long userId, CommentDto commentDto);
}
