package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.item.ItemDto;

@Slf4j
public final class ItemChecker {
    public static void itemChecker(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            log.error("имя предмета не может быть пустым");
            throw new ValidationException("имя предмета не может быть пустым");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            log.error("описание предмета не может быть пустым");
            throw new ValidationException("описание предмета не может быть пустым");
        }
        if (itemDto.getAvailable() == null) {
            log.error("состояние предмета не может быть пустым");
            throw new ValidationException("состояние предмета не может быть пустым");
        }
    }
}
