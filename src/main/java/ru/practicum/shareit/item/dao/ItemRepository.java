package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface ItemRepository {
    Collection<Item> getAllItems();

    Collection<Item> getAllItemsOfUser(Long userId);

    Optional<Item> getItemById(Long id);

    Item create(Item item, Long userId);

    Item update(Item item, Map<String, String> fields);
}
