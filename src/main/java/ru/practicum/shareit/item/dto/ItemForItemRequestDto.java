package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
@Builder
public class ItemForItemRequestDto {
    Long id;

    @NotNull(message = "Должно быть указано имя вещи")
    @NotBlank(message = "Должно быть указано имя вещи")
    String name;

    User owner;
}
