package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;


@Data
@Builder
public class ItemWithBookingDto {
    Long id;

    @NotNull(message = "Должно быть указано имя вещи")
    @NotBlank(message = "Должно быть указано имя вещи")
    String name;

    @NotNull(message = "Должно быть указано описание вещи")
    String description;

    @NotNull(message = "Должен быть указан статус о том, доступна или нет вещь для аренды")
    Boolean available;

    User owner;

    List<BookingDtoShort> bookings;

    ItemRequest request;

    List<CommentDto> comments;
}