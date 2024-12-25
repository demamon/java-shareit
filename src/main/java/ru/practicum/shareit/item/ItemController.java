package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ItemChecker;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemServiceImpl itemService;

    @Autowired
    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody ItemDto itemDto) {
        log.trace("добавляем новый предмет {} от пользователя с id {}", itemDto, userId);
        ItemChecker.itemChecker(itemDto);
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long itemId,
                          @RequestBody UpdateItemRequest request) {
        log.trace("обновляем предмет с id {}. Текст {}. Обновляет юзер с id {}", itemId, request, userId);
        return itemService.update(userId, itemId, request);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemId(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long itemId) {
        log.trace("возвращаем предмет с id {}", itemId);
        return itemService.findItemId(itemId);
    }

    @GetMapping
    public Collection<ItemDto> findItemUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.trace("Возвращаем все предметы пользователя с id {}", userId);
        return itemService.findItemUserId(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findItemText(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam Optional<String> text) {
        if (text.isEmpty() || text.get().isBlank()) {
            return new ArrayList<>();
        }
        log.trace("возвращаем все предметы у которых имя или описание совпадает с текстом {}", text);
        return itemService.findItemText(text.get());
    }
}
