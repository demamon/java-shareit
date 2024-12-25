package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item save(Item item) {
        item.setId(getNextId());
        log.debug("Добавляем предмет {}", item);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item findItemId(long id) {
        Item item = items.get(id);
        log.debug("Возвращаем предмет {}", item);
        if (item == null) {
            throw new NotFoundException("Предмета с id = " + id + " не существует");
        }
        return item;
    }

    @Override
    public Item update(Item updateItem) {
        items.put(updateItem.getId(), updateItem);
        log.debug("Обновляем предмет {}", updateItem);
        return updateItem;
    }

    @Override
    public Collection<Item> findItemUserId(long userId) {
        return items.values().stream()
                .filter(item -> item.getIdOwner() == userId)
                .toList();
    }

    @Override
    public Collection<Item> findItemText(String text) {
        return items.values().stream()
                .filter(item -> item.getAvailable() &&
                        (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .toList();
    }

    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
