package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.StatusOfBooking;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoShort {

    @NotNull(message = "Должны быть указаны дата и время начала бронирования")
    @FutureOrPresent(message = "Дата и время начала бронирования должны быть указаны в текущем или будущем времени")
    LocalDateTime start;

    @NotNull(message = "Должны быть указаны дата и время конца бронирования")
    @Future(message = "Дата и время конца бронирования должны быть указаны в будущем времени")
    LocalDateTime end;

    @NotNull(message = "Должен быть указан id вещи, которую пользователь бронирует")
    Long itemId;
    StatusOfBooking status;
}
