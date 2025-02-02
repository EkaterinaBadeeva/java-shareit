package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.Map;

public interface ItemService {
    Collection<ItemDto> getAllItemsOfUser(Long userId);

    ItemDto getItemById(Long id, Long userId);

    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto update(Map<String, String> fields, Long itemId, Long userId);

    Collection<ItemDto> findItem(String text);
}
