package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.Instant;

@Data
@Builder
public class CommentDto {
    Long id;
    String text;
    Item item;
    String authorName;
    Instant created;
}
