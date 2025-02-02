package ru.practicum.shareit.request.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

/**
 * TODO Sprint add-item-requests.
 */

@Data
public class ItemRequest {
    //id — уникальный идентификатор запроса
    Long id;

    //description — текст запроса, содержащий описание требуемой вещи
    @NotNull
    @NotBlank
    String description;

    //requestor — пользователь, создавший запрос
    @NotNull
    @NotBlank
    User requestor;

    //created — дата и время создания запроса
    Instant created;

}
