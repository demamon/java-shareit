package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDto;

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
    }
}
