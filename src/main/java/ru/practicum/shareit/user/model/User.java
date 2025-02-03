package ru.practicum.shareit.user.model;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class User {
    //id — уникальный идентификатор пользователя
    Long id;

    //name — имя или логин пользователя
    String name;

    //email — адрес электронной почты
    String email;
}



