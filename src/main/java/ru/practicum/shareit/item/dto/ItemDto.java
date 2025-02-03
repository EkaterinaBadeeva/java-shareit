package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Review;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class ItemDto {
    Long id;

    @NotNull
    @NotBlank
    String name;

    @NotNull
    String description;

    @NotNull
    Boolean available;

    User owner;

    ItemRequest request;
    List<Review> reviews;
}
