package ru.practicum.shareit.request.dto;

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
    String description;
    List<ItemForItemRequestDto> items;
    UserWithOnlyNameDto requestor;
    Instant created;
}
