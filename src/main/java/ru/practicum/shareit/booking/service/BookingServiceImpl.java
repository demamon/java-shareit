package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dao.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Autowired
    public BookingServiceImpl(BookingStorage bookingStorage, UserStorage userStorage, ItemStorage itemStorage) {
        this.bookingStorage = bookingStorage;
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
    }

    @Override
    @Transactional
    public BookingResponseDto create(Long userId, BookingDto bookingDto) {
        log.trace("Создание бронирования - {} вещи с id - {}", bookingDto, userId);
        User user = userStorage.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь не найден"));
        Item item = itemStorage.findById(bookingDto.getItemId()).orElseThrow(()
                -> new NotFoundException("Предмет не найден"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования");
        }
        log.debug("Передаем данные в маппер");
        Booking booking = BookingMapper.mapToBooking(bookingDto, user, item);
        System.out.println("Сохраняем бронирование");
        System.out.println(booking);
        bookingStorage.save(booking);
        log.debug("Бронирование сохранили");
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingResponseDto approveBooking(Long bookingId, Long userId, Boolean approved) {
        log.trace("Подтверждение/отклонение бронирования с id - {} пользователем с id - {}", bookingId, userId);
        Booking booking = bookingStorage.findById(bookingId).orElseThrow(()
                -> new NotFoundException("Бронирование не найдено"));
        Long itemUserId = booking.getItem().getOwner().getId();
        if (!userId.equals(itemUserId)) {
            throw new ValidationException("Пользователь не является владельцем вещи");
        }
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Бронирование уже подтверждено или отклонено");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        Booking savedBooking = bookingStorage.save(booking);
        return BookingMapper.mapToBookingDto(savedBooking);
    }

    @Override
    public BookingResponseDto findBookingById(Long bookingId, Long userId) {
        log.trace("Получение бронирования с id - {} пользователем с id - {}", bookingId, userId);
        User user = userStorage.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Booking booking = bookingStorage.findById(bookingId).orElseThrow(()
                -> new NotFoundException("Бронирование не найдено"));
        log.debug("пользователь запрашивающий бронь - {}", user);
        log.debug("запрашиваемая бронь - {}", booking);
        if (!userId.equals(booking.getUser().getId()) && (!userId.equals(booking.getItem().getOwner().getId()))) {
            log.error("Пользователь с id " + userId + "не владелец вещи и не автор бронирования");
            throw new ValidationException("Пользователь с id " + userId + "не владелец вещи и не автор бронирования");
        }
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingResponseDto> findAllBookingsByUser(Long userId, State state) {
        log.trace("Получение всех бронирований пользователя с id - {}", userId);
        LocalDateTime now = LocalDateTime.now();
        userStorage.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        List<Booking> bookings = switch (state) {
            case PAST -> bookingStorage.findByUserIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE -> bookingStorage.findByUserIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING -> bookingStorage.findByUserIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case REJECTED -> bookingStorage.findByUserIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            case CURRENT -> bookingStorage.findCurrentBookings(userId, now);
            default -> bookingStorage.findByUserIdOrderByStartDesc(userId);
        };
        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> findAllBookingsByItems(Long userId, State state) {
        log.trace("Получение всех бронирований всех вещей пользователя с id - {}", userId);
        userStorage.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        List<Booking> bookings = switch (state) {
            case PAST, REJECTED, FUTURE, WAITING, CURRENT ->
                    bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, state);
            default -> bookingStorage.findAllByItemOwnerIdOrderByStartDesc(userId);
        };
        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
    }
}
