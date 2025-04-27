package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

@Slf4j
public final class BookingChecker {
    public static void bookingChecker(BookingDto bookingDto) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            log.error("временной отрезок не может быть пустым");
            throw new ValidationException("временной отрезок не может быть пустым");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            log.error("время начала аренды не может быть позже старта");
            throw new ValidationException("время начала аренды не может быть позже старта");
        }
        if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            log.error("время начала не может совпадать со временем конца аренды");
            throw new ValidationException("время начала не может совпадать со временем конца аренды");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            log.error("время начала аренды не может быть в прошлом");
            throw new ValidationException("время начала аренды не может быть в прошлом");
        }
        if (!(bookingDto.getEnd().isAfter(LocalDateTime.now()))) {
            log.error("время окончания аренды не находится в будущем.");
            throw new ValidationException("время окончания аренды не находится в будущем.");
        }
    }
}
