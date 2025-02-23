package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.State;

import java.util.Collection;

public interface BookingService {
    BookingDto create(BookingDtoShort bookingDto, Long userId);

    BookingDto approved(Boolean approved, Long bookingId, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);

    Collection<BookingDto> findBookingByBooker(State state, Long userId);

    Collection<BookingDto> findBookingByOwner(State state, Long userId);
}
