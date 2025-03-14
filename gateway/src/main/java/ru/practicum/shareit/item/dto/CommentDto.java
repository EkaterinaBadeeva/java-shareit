package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    Long id;
    String text;
    String authorName;
    Instant created;
}
