package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public static Booking mapToBooking(BookingDto bookingDto, User user, Item item) {
        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setUser(user);
        booking.setItem(item);
        return booking;
    }

    public static BookingResponseDto mapToBookingDto(Booking booking) {
        log.debug("Передаем {} в маппер", booking);
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setStart(booking.getStart());
        bookingResponseDto.setEnd(booking.getEnd());
        bookingResponseDto.setItem(booking.getItem());
        bookingResponseDto.setBooker(booking.getUser());
        bookingResponseDto.setStatus(booking.getStatus());
        log.debug("Отдаем {}, после маппера", bookingResponseDto);
        return bookingResponseDto;
    }
}
