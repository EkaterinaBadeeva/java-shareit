package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
   private final ItemRequestClient itemRequestClient;

    //POST /requests
    // добавление нового запроса на вещь
    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Добавление нового запроса на вещь: request {}, userId={}", itemRequestDto, userId);
        return itemRequestClient.create(itemRequestDto, userId);
    }

    //GET /requests
    // получение списка своих запросов вместе с данными об ответах на них
    @GetMapping
    public ResponseEntity<Object> getItemRequestsOfRequestor(@RequestHeader("X-Sharer-User-Id") Long userId) {
    log.info("Получение списка своих запросов вместе с данными об ответах на них: userId={}", userId);
        return itemRequestClient.getItemRequestsOfRequestor(userId);
    }

    //GET /requests/all
    // получение списка запросов, созданных другими пользователями
    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsOfAllUsers(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение списка запросов, созданных другими пользователями: userId={}", userId);
        return itemRequestClient.getItemRequestsOfAllUsers(userId);
    }

    //GET /requests/{requestId}
    // получение данных об одном конкретном запросе вместе с данными об ответах на него
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@Valid @PathVariable Long requestId,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение данных об одном конкретном запросе вместе с данными об ответах на него: requestId={}, userId={}", requestId, userId);
        return itemRequestClient.getItemRequestById(requestId, userId);
    }
}
