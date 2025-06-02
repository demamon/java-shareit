package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto create(long userId, String description);

    Collection<ItemRequestDto> getAllRequest(long userId);

    Collection<RequestWithItemDto> getRequests(long userId);

    RequestWithItemDto getRequest(long requestId);
}
