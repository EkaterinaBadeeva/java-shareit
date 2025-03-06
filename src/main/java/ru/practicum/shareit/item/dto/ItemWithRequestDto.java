package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemWithRequestDto {

    @NotNull(message = "Должно быть указано имя вещи")
    @NotBlank(message = "Должно быть указано имя вещи")
    String name;

    @NotNull(message = "Должно быть указано описание вещи")
    String description;

    @NotNull(message = "Должен быть указан статус о том, доступна или нет вещь для аренды")
    Boolean available;

    Long requestId;
}