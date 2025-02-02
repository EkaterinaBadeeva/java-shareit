package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@EqualsAndHashCode(of = {"email"})
public class User {
    //id — уникальный идентификатор пользователя
    Long id;

    //name — имя или логин пользователя
    @NotNull
    @NotBlank
    String name;

    //email — адрес электронной почты
    @NotNull
    @Email
    String email;
}



