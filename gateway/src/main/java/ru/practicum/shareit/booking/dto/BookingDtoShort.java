package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.validator.StartBeforeEnd;

import java.time.LocalDateTime;

@Getter
@StartBeforeEnd
@NoArgsConstructor
@AllArgsConstructor
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
