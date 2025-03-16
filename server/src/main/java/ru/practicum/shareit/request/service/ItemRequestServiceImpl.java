package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ItemRequestDto create(ItemRequestDto itemRequestDto, Long userId) {
        log.info("Добавление нового запроса на вещь.");
        checkId(userId);

        User requestor = UserMapper.mapToUser(userService.findUserById(userId));
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto);
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(Instant.now());
        itemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestWithItemDto> getItemRequestsOfRequestor(Long userId) {
        log.info("Получение списка своих запросов вместе с данными об ответах на них.");
        checkId(userId);
        checkUser(userId);

        List<ItemRequestWithItemDto> itemRequestsOfRequestorWithItem = new ArrayList<>();
        List<ItemRequest> itemRequests = itemRequestRepository.findAllItemRequestByRequestorIdOrderByCreatedDesc(userId);

        List<Long> itemRequestIds = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequestIds.add(itemRequest.getId());
        }

        List<Item> items = itemRepository.findAllItemsByRequestIdIn(itemRequestIds);

        List<ItemForItemRequestDto> itemsDto = new ArrayList<>();
        Map<Long, List<ItemForItemRequestDto>> itemsWithRequest = new HashMap<>();

        for (Item item : items) {
            itemsDto.add(ItemMapper.mapItemForItemRequestDto(item));
            itemsWithRequest.put(item.getRequest().getId(), itemsDto);
        }

        for (ItemRequest itemRequest : itemRequests) {
            List<ItemForItemRequestDto> itemsForRequest = itemsWithRequest.get(itemRequest.getId());
            itemRequestsOfRequestorWithItem.add(ItemRequestMapper.mapToItemRequestWithItemDto(itemRequest, itemsForRequest));
        }

        return itemRequestsOfRequestorWithItem;
    }

    @Override
    public List<ItemRequestDto> getItemRequestsOfAllUsers(Long userId) {
        log.info("Получение списка запросов, созданных другими пользователями.");
        checkId(userId);
        checkUser(userId);

        List<ItemRequestDto> itemRequestDto = itemRequestRepository.findAllItemRequestByRequestorIdNotOrderByCreatedDesc(userId)
                .stream()
                .map(ItemRequestMapper::mapToItemRequestDto)
                .toList();

        return itemRequestDto;
    }

    @Override
    public ItemRequestWithItemDto getItemRequestById(Long requestId, Long userId) {
        log.info("Получение данных об одном конкретном запросе вместе с данными об ответах на него.");
        checkId(requestId);
        checkId(userId);
        checkUser(userId);

        ItemRequestWithItemDto itemRequestWithItem;
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("запрос с id " + requestId + " не найден"));

        List<ItemForItemRequestDto> items = itemRepository.findAllItemsByRequestId(requestId).stream()
                .map(ItemMapper::mapItemForItemRequestDto)
                .toList();

        itemRequestWithItem = ItemRequestMapper.mapToItemRequestWithItemDto(itemRequest, items);

        return itemRequestWithItem;
    }

    private void checkId(Long id) {
        if (id == null) {
            log.warn("Id должен быть указан.");
            throw new ValidationException("Id должен быть указан");
        }
    }

    private void checkUser(Long userId) {
        if (userService.findUserById(userId) == null) {
            log.warn("Неверно указан Id пользователя.");
            throw new NotFoundException("Пользователь с Id = " + userId + " не найден");
        }
    }
}
