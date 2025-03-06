package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@Builder
public class ItemRequestDto {

    Long id;

    @NotNull(message = "Должен быть указан текст запроса, содержащий описание требуемой вещи")
    @NotBlank(message = "Должен быть указан текст запроса, содержащий описание требуемой вещи")
    String description;

    User requestor;
    Instant created;
}
