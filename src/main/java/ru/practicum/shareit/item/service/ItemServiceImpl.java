package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.CommentStorage;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.CommentMapper;
import ru.practicum.shareit.item.dto.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.item.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;
    private final CommentStorage commentStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage, BookingStorage bookingStorage,
                           CommentStorage commentStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.bookingStorage = bookingStorage;
        this.commentStorage = commentStorage;
    }

    @Override
    @Transactional
    public ItemDto create(long userId, ItemDto itemDto) {
        Optional<User> mayBeUser = userStorage.findById(userId);
        if (mayBeUser.isEmpty()) {
            throw new NotFoundException("пользователя с id = " + userId + "не существует");
        }
        User user = mayBeUser.get();
        log.debug("Предмет формата dto {}", itemDto);
        Item item = ItemMapper.mapToItem(itemDto, user);
        log.debug("Предмет после форматирования {}", item);
        item = itemStorage.save(item);
        log.debug("Предмет после сохранения {}", item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    @Transactional
    public ItemDto update(long userId, long itemId, UpdateItemRequest request) {
        Optional<Item> mayBeItem = itemStorage.findById(itemId);
        if (mayBeItem.isEmpty()) {
            throw new NotFoundException("предмет с id = " + itemId + " не найден");
        }
        Item updateItem = mayBeItem.get();
        if (updateItem.getOwner().getId() != userId) {
            throw new NotFoundException("предмет с id = " + itemId + " не найден у пользователя с id = " + userId);
        }
        log.debug("Предмет до обновления {}", updateItem);
        log.debug("Обновляемые поля {}", request);
        ItemMapper.updateItemField(updateItem, request);
        updateItem = itemStorage.save(updateItem);
        log.debug("Предмет после обновления {}", updateItem);
        return ItemMapper.mapToItemDto(updateItem);
    }

    @Override
    public ItemCommentsDto findItemId(long itemId, Long userId) {
        Optional<Item> mayBeItem = itemStorage.findById(itemId);
        if (mayBeItem.isEmpty()) {
            throw new NotFoundException("предмет с id = " + itemId + " не найден");
        }
        Item item = mayBeItem.get();
        List<CommentResponseDto> commentList = commentStorage.findAllByItemId(itemId).stream()
                .map(CommentMapper::mapToCommentDto)
                .toList();
        BookingResponseDto lastBooking = null;
        BookingResponseDto nextBooking = null;
        List<Booking> lastBookings = bookingStorage.findAllByItemIdAndEndBeforeAndStatusOrderByEndDesc(itemId,
                LocalDateTime.now(), Status.APPROVED);
        log.debug("последняя бронь {}", lastBookings);
        if (!lastBookings.isEmpty() && userId.equals(item.getOwner().getId())) {
            lastBooking = BookingMapper.mapToBookingDto(lastBookings.getFirst());
        }
        List<Booking> nextBookings = bookingStorage.findAllByItemIdAndStartAfterOrderByStartAsc(itemId,
                LocalDateTime.now());
        log.debug("следующая бронь {}", nextBookings);
        if (!nextBookings.isEmpty() && userId.equals(item.getOwner().getId())) {
            nextBooking = BookingMapper.mapToBookingDto(nextBookings.getFirst());
        }
        log.debug("брони после маппера {} {}", lastBooking, nextBooking);
        return ItemMapper.mapToItemCommentsDto(item, commentList, lastBooking, nextBooking);
    }

    @Override
    public Collection<ItemsDto> findItemUserId(long userId) {
        if (userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("пользователя с id = " + userId + "не существует");
        }
        Collection<Item> items = itemStorage.findByOwnerId(userId);
        log.debug("список вещей {}", items);
        List<Long> itemIds = items.stream().map(Item::getId).toList();
        Map<Long, List<Booking>> bookingsByItem = bookingStorage.findByItemIdIn(itemIds)
                .stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));
        log.debug("список бронирований {}", bookingsByItem);
        Map<Long, List<Comment>> commentsByItem = commentStorage.findByItemIdIn(itemIds)
                .stream()
                .collect(Collectors.groupingBy(comment -> comment.getAuthor().getId()));
        log.debug("список комментариев {}", commentsByItem);
        return items.stream()
                .map(item -> {
                    List<Booking> bookingList = bookingsByItem.getOrDefault(item.getId(), List.of());
                    Optional<LocalDate> lastBooking = bookingList.stream()
                            .max(Comparator.comparing(Booking::getStart))
                            .map(booking -> booking.getStart().toLocalDate());
                    Optional<LocalDate> nextBooking = bookingList.stream()
                            .min(Comparator.comparing(Booking::getStart))
                            .map(booking -> booking.getStart().toLocalDate());
                    List<CommentResponseDto> commentList = commentsByItem.getOrDefault(item.getId(), List.of())
                            .stream()
                            .map(CommentMapper::mapToCommentDto)
                            .toList();

                    return ItemMapper.mapToItemsDto(item, lastBooking, nextBooking, commentList);
                })
                .toList();
    }

    @Override
    public Collection<ItemDto> findItemText(String text) {
        log.trace("Получение списка вещей содержащие текст - {} в названии или описании", text);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        String editedText = text.toLowerCase();
        return itemStorage.findAll().stream()
                .filter(Item::getAvailable)
                .filter(item ->
                        (item.getName() != null && item.getName().toLowerCase().contains(editedText)) ||
                                (item.getDescription() != null && item.getDescription().toLowerCase().contains(editedText))
                )
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentResponseDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        log.trace("Добавление отзыва к вещи с id - {}", itemId);
        User user = userStorage.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь не найден"));
        Item item = itemStorage.findById(itemId).orElseThrow(()
                -> new NotFoundException("Вещь не найдена"));
        List<Booking> bookings = bookingStorage.findByItemIdAndUserIdAndStatusAndEndBefore(itemId, userId,
                Status.APPROVED, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new ValidationException("Пользователь не брал в аренду эту вещь");
        }
        Comment comment = CommentMapper.mapToComment(commentDto, user, item);
        comment.setCreated(LocalDateTime.now());
        Comment savedComment = commentStorage.save(comment);
        return CommentMapper.mapToCommentDto(savedComment);
    }
}
