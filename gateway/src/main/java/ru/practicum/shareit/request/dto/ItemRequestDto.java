package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    Long id;

    @NotNull(message = "Должен быть указан текст запроса, содержащий описание требуемой вещи")
    @NotBlank(message = "Должен быть указан текст запроса, содержащий описание требуемой вещи")
    String description;

    Instant created;
}
