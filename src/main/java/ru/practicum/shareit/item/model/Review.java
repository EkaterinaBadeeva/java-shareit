package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class Review {
    //id — уникальный отзыва о вещи
    Long id;

    //content — содержание отзыва
    String content;
}
