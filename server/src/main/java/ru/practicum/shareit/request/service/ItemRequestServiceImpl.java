package ru.practicum.shareit.request.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestStorage;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.RequestWithItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestStorage itemRequestStorage, UserStorage userStorage, ItemStorage itemStorage) {
        this.itemRequestStorage = itemRequestStorage;
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
    }

    @Override
    @Transactional
    public ItemRequestDto create(long userId, String description) {
        Optional<User> mayBeUser = userStorage.findById(userId);
        if (mayBeUser.isEmpty()) {
            throw new NotFoundException("пользователя с id = " + userId + "не существует");
        }
        User user = mayBeUser.get();
        ItemRequest itemRequest = ItemRequestMapper.mapToRequest(user, description);
        itemRequest = itemRequestStorage.save(itemRequest);
        return ItemRequestMapper.mapToRequestDto(itemRequest);
    }

    @Override
    public Collection<ItemRequestDto> getAllRequest(long userId) {
        return itemRequestStorage.findByAuthorIdNotOrderByCreatedDesc(userId).stream()
                .map(ItemRequestMapper::mapToRequestDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<RequestWithItemDto> getRequests(long userId) {
        Optional<User> mayBeUser = userStorage.findById(userId);
        if (mayBeUser.isEmpty()) {
            throw new NotFoundException("пользователя с id = " + userId + "не существует");
        }
        List<ItemRequest> itemRequestList = itemRequestStorage.findByAuthorIdOrderByCreatedDesc(userId);
        log.debug("список запросов {}", itemRequestList);
        List<Long> requestId = itemRequestList.stream().map(ItemRequest::getId).toList();
        Map<Long, List<Item>> itemsByRequest = itemStorage.findByItemRequestIdIn(requestId).stream()
                .collect(Collectors.groupingBy(item -> item.getItemRequest().getId()));
        log.debug("списки предметов к списку запросов {}", itemsByRequest);

        return itemRequestList.stream()
                .map(itemRequest -> {
                            List<Item> items = itemsByRequest.getOrDefault(itemRequest.getId(), List.of());
                            return ItemRequestMapper.mapToRequestWithItem(itemRequest, items);
                        }
                )
                .toList();
    }

    @Override
    public RequestWithItemDto getRequest(long requestId) {
        Optional<ItemRequest> mayBeRequest = itemRequestStorage.findById(requestId);
        if (mayBeRequest.isEmpty()) {
            throw new NotFoundException("запроса с id = " + requestId + "не существует");
        }
        ItemRequest request = mayBeRequest.get();
        log.debug("запрос из базы {}", request);
        List<Item> items = itemStorage.findByItemRequestId(requestId);
        log.debug("список предметов для запроса из базы {}", items);
        return ItemRequestMapper.mapToRequestWithItem(request, items);

    }

}
