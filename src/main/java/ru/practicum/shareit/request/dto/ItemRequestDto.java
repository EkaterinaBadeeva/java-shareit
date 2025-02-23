package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

/**
 * TODO Sprint add-item-requests.
 */

public class ItemRequestDto {

    Long id;

    @NotNull(message = "Должен быть указан текст запроса, содержащий описание требуемой вещи")
    @NotBlank(message = "Должен быть указан текст запроса, содержащий описание требуемой вещи")
    String description;

    @NotNull(message = "Должен быть указан пользователь, создавший запрос")
    @NotBlank(message = "Должен быть указан пользователь, создавший запрос")
    User requestor;

    Instant created;
}
