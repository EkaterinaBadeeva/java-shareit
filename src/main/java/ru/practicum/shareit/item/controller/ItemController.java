package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    //GET /items
    // просмотр владельцем списка всех его вещей
    @GetMapping
    public Collection<ItemWithBookingDto> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemsOfUser(userId);
    }

    //GET /items/{itemId}
    // найти вещь по Id
    @GetMapping(value = "/{id}")
    public ItemDto getItemById(@PathVariable Long id,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(id, userId);
    }

    //POST /items
    // добавить вещь
    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.create(itemDto, userId);
    }

    //PATCH /items/{itemId}
    // отредактировать вещь
    @PatchMapping("/{itemId}")
    public ItemDto update(@Valid @RequestBody Map<String, String> fields,
                          @PathVariable Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.update(fields, itemId, userId);
    }

    //GET /items/search?text={text}
    // поиск вещи потенциальным арендатором
    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@Valid @RequestParam String text) {
        return itemService.searchItem(text);
    }

    //POST /items/{itemId}/comment
    // добавление комментария о вещи после аренды
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment (@Valid @RequestBody CommentDto commentDto ,
                        @PathVariable Long itemId,
                        @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.createComment(commentDto, itemId, userId);
    }
}