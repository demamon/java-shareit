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
        BookingResponseDto bookingResponseDto = BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getUser())
                .status(booking.getStatus())
                .build();
        log.debug("Отдаем {}, после маппера", bookingResponseDto);
        return bookingResponseDto;
    }
}
