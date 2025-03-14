package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    //GET /items
    // просмотр владельцем списка всех его вещей
    @GetMapping
    public ResponseEntity<Object> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Просмотр владельцем списка всех его вещей: userId={}", userId);
        return itemClient.getAllItemsOfUser(userId);
    }

    //GET /items/{itemId}
    // найти вещь по Id
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable Long id,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
         log.info("Найти вещь по id: itemId={}, userId={}", id, userId);
        return itemClient.getItemById(id, userId);
    }

    //POST /items
    // добавить вещь
    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemWithRequestDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
         log.info("Добавить вещь: item={}, userId={}", itemDto, userId);
        return itemClient.create(itemDto, userId);
    }

    //PATCH /items/{itemId}
    // отредактировать вещь
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@Valid @RequestBody Map<String, String> fields,
                          @PathVariable Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
         log.info("Отредактировать вещь: itemId={}, userId={}", itemId, userId);
        return itemClient.update(fields, itemId, userId);
    }

    //GET /items/search?text={text}
    // поиск вещи потенциальным арендатором
    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@Valid @RequestParam String text) {
         log.info("Поиск вещи потенциальным арендатором");
        return itemClient.searchItem(text);
    }

    //POST /items/{itemId}/comment
    // добавление комментария о вещи после аренды
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                                    @PathVariable Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
         log.info("Добавление комментария о вещи после аренды: comment={}, itemId={}, userId={}", commentDto, itemId, userId);
        return itemClient.createComment(commentDto, itemId, userId);
    }
}
