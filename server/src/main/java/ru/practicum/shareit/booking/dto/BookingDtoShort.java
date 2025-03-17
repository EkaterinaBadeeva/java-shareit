package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.StatusOfBooking;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoShort {

    LocalDateTime start;

    LocalDateTime end;

    Long itemId;
    StatusOfBooking status;
}
