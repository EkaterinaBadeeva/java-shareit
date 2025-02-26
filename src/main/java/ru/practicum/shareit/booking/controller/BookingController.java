package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    //POST /bookings
    // добавление нового запроса на бронирование
    @PostMapping
    public BookingDto create(@Valid @RequestBody BookingDtoShort bookingDto,
                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.create(bookingDto, userId);
    }

    //PATCH /bookings/{bookingId}?approved={approved}
    //подтверждение или отклонение запроса на бронирование
    @PatchMapping("/{bookingId}")
    public BookingDto approved(@Valid @RequestParam Boolean approved,
                               @PathVariable Long bookingId,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.approved(approved, bookingId, userId);
    }

    //GET /bookings/{bookingId}
    //получение запроса на бронирование по Id
    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    //GET /bookings?state={state}
    //получение списка всех бронирований текущего пользователя
    @GetMapping
    public Collection<BookingDto> findBookingByBooker(@Valid @RequestParam(defaultValue = "ALL") State state,
                                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.findBookingByBooker(state, userId);
    }

    //GET /bookings/owner?state={state}
    //получение списка всех бронирований текущего пользователя
    @GetMapping("/owner")
    public Collection<BookingDto> findBookingByOwner(@Valid @RequestParam(defaultValue = "ALL") State state,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.findBookingByOwner(state, userId);
    }
}
