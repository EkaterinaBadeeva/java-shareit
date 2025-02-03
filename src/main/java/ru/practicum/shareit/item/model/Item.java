package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class Item {
    //id — уникальный идентификатор вещи
    Long id;

    //name — краткое название
    String name;

    //description — развёрнутое описание
    String description;

    //available — статус о том, доступна или нет вещь для аренды
    Boolean available;

    //owner — владелец вещи
    User owner;

    //request — если вещь была создана по запросу другого пользователя, то в этом
    //поле будет храниться ссылка на соответствующий запрос
    ItemRequest request;

    //reviews — отзывы о вещи
    List<Review> reviews;
}