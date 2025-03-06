package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.user.dto.UserWithOnlyNameDto;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ItemRequestWithItemDto {
    Long id;

    @NotNull(message = "Должен быть указан текст запроса, содержащий описание требуемой вещи")
    @NotBlank(message = "Должен быть указан текст запроса, содержащий описание требуемой вещи")
    String description;

    List<ItemForItemRequestDto> items;
    UserWithOnlyNameDto requestor;
    Instant created;
}
