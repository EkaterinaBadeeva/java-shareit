package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */

@Data
public class Booking {
    //id — уникальный идентификатор бронирования
    Long id;

    //start — дата и время начала бронирования
    Instant start;
    //end — дата и время конца бронирования
    Instant end;

    //item — вещь, которую пользователь бронирует
    @NotNull
    @NotBlank
    Item item;

    //booker — пользователь, который осуществляет бронирование
    @NotNull
    @NotBlank
    User booker;

    //status — статус бронирования
    StatusOfBooking status;
}


