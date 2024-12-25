package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item save(Item item);

    Item findItemId(long id);

    Item update(Item updateItem);

    Collection<Item> findItemUserId(long userId);

    Collection<Item> findItemText(String text);
}
