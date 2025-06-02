package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private final ItemRequestServiceImpl itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestServiceImpl itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody ItemRequestDto description) {
        log.trace("добавляем новый запрос на предмет {}, от пользователя с id = {}", description.getDescription(), userId);
        return itemRequestService.create(userId, description.getDescription());
    }

    @GetMapping("/all")
    Collection<ItemRequestDto> getAllRequest(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.trace("возвращаем все запросы, кроме запросов пользователя c id = {}", userId);
        return itemRequestService.getAllRequest(userId);
    }

    @GetMapping
    Collection<RequestWithItemDto> getRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.trace("возвращаем список запросов пользователя с id = {}, вместе с ответами на запрос", userId);
        return itemRequestService.getRequests(userId);
    }

    @GetMapping("/{requestId}")
    RequestWithItemDto getRequest(@PathVariable long requestId) {
        log.trace("возвращаем запрос по id = {}", requestId);
        return itemRequestService.getRequest(requestId);
    }

}
