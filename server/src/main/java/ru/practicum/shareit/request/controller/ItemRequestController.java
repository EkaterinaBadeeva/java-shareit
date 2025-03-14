package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    //POST /requests
    // добавление нового запроса вещь
    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.create(itemRequestDto, userId);
    }

    //GET /requests
    // получение списка своих запросов вместе с данными об ответах на них
    @GetMapping
    public Collection<ItemRequestWithItemDto> getItemRequestsOfRequestor(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequestsOfRequestor(userId);
    }

    //GET /requests/all
    // получение списка запросов, созданных другими пользователями
    @GetMapping("/all")
    public Collection<ItemRequestDto> getItemRequestsOfAllUsers(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequestsOfAllUsers(userId);
    }

    //GET /requests/{requestId}
    // получение данных об одном конкретном запросе вместе с данными об ответах на него
    @GetMapping("/{requestId}")
    public ItemRequestWithItemDto getItemRequestById(@PathVariable Long requestId,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequestById(requestId, userId);
    }

}
