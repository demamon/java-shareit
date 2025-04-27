package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.BookingChecker;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private final BookingServiceImpl bookingService;

    @Autowired
    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponseDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestBody BookingDto bookingDto) {
        log.trace("добавляем новую бронь {} от пользователя с id {}", bookingDto, userId);
        System.out.println(bookingDto);
        BookingChecker.bookingChecker(bookingDto);
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@PathVariable Long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestParam Boolean approved) {
        return bookingService.approveBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto findBookingById(@PathVariable Long bookingId,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.findBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> findAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.findAllBookingsByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> findAllBookingsByItems(@RequestHeader("X-Sharer-User-Id") Long userid,
                                                           @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.findAllBookingsByItems(userid, state);
    }

}
