package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        userStorage.findUserId(userId);
        log.debug("Предмет формата dto {}", itemDto);
        Item item = ItemMapper.mapToItem(itemDto, userId);
        log.debug("Предмет после форматирования {}", item);
        item = itemStorage.save(item);
        log.debug("Предмет после сохранения {}", item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto update(long userId, long itemId, UpdateItemRequest request) {
        Item updateItem = itemStorage.findItemId(itemId);
        if (updateItem.getIdOwner() != userId) {
            throw new NotFoundException("предмет с id = " + itemId + " не принадлежит пользователю с id = " + userId);
        }
        log.debug("Предмет до обновления {}", updateItem);
        log.debug("Обновляемые поля {}", request);
        ItemMapper.updateItemField(updateItem, request);
        updateItem = itemStorage.update(updateItem);
        log.debug("Предмет после обновления {}", updateItem);
        return ItemMapper.mapToItemDto(updateItem);
    }

    @Override
    public ItemDto findItemId(long itemId) {
        Item item = itemStorage.findItemId(itemId);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public Collection<ItemDto> findItemUserId(long userId) {
        return itemStorage.findItemUserId(userId).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> findItemText(String text) {
        return itemStorage.findItemText(text).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }
}
