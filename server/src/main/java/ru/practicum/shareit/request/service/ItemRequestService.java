package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestWithItemDto> getItemRequestsOfRequestor(Long userId);

    List<ItemRequestDto> getItemRequestsOfAllUsers(Long userId);

    ItemRequestWithItemDto getItemRequestById(Long requestId, Long userId);
}
