package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.Collection;
import java.util.Map;

public interface ItemService {
    Collection<ItemWithBookingDto> getAllItemsOfUser(Long userId);

    ItemDto getItemById(Long id, Long userId);

    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto update(Map<String, String> fields, Long itemId, Long userId);

    Collection<ItemDto> searchItem(String text);

    CommentDto createComment(CommentDto commentDto, Long itemId, Long userId);
}
