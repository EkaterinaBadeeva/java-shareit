package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

@Data
@Builder
public class ItemRequestDto {
    Long id;
    String description;
    User requestor;
    Instant created;
}
