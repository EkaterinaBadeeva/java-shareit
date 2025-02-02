package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public Collection<ItemDto> getAllItemsOfUser(Long userId) {
        log.info("Получение списка всех вещей.");
        return itemRepository.getAllItemsOfUser(userId).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long id, Long userId) {
        log.info("Получение вещи по Id.");
        checkId(id);
        checkId(userId);

        return itemRepository.getItemById(id)
                .map(ItemMapper::mapToItemDto)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + id + " не найдена"));
    }


    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        log.info("Добавление новой вещи.");
        checkId(userId);

        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwner(UserMapper.mapToUser(userService.findUserById(userId)));
        checkConditions(item, userId);
        item = itemRepository.create(item, userId);
        checkId(item.getId());
        return ItemMapper.mapToItemDto(item);
    }


    @Override
    public ItemDto update(Map<String, String> fields, Long itemId, Long userId) {
        log.info("Редактирование вещи.");
        checkId(itemId);
        checkId(userId);

        Item oldItem = ItemMapper.mapToItem(getItemById(itemId, userId));

        if (!oldItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Вещь " + oldItem.getName() + " в списке пользователя" +
                    " не найдена");
        }

        Item item = itemRepository.update(oldItem, fields);

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public Collection<ItemDto> findItem(String text) {
        log.info("Поиск вещи потенциальным арендатором");
        Collection<Item> items = itemRepository.getAllItems();
        Collection<Item> findingItems = new ArrayList<>();

        if (text == null) {
            log.warn("Текст поиска должен быть указан.");
            throw new ValidationException("Текст поиска должен быть указан");
        }

        if (text.isEmpty()) {
            return List.of();
        }

        items.stream().filter(item -> item.getAvailable() && (item.getName().toUpperCase().contains(text.toUpperCase()) ||
                item.getDescription().toUpperCase().contains(text.toUpperCase()))).forEach(item -> {
            log.info("Вещь доступна для аренды и содержит текст поиска.");
            findingItems.add(item);
        });

        return findingItems.stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    private void checkId(Long id) {
        if (id == null) {
            log.warn("Id должен быть указан.");
            throw new ValidationException("Id должен быть указан");
        }
    }

    private void checkConditions(Item item, Long userId) {

        if (item.getName().isEmpty()) {
            log.warn("Задано пустое имя вещи.");
            throw new ValidationException("Задано пустое имя вещи");
        }

        if (item.getAvailable() == null) {
            log.warn("Задан пустой статус о том, доступна или нет вещь для аренды.");
            throw new ValidationException("Задан пустой статус о том, доступна или нет вещь для аренды");
        }

        if (item.getOwner() == null) {
            log.warn("Задано пустое поле - владелец вещи.");
            throw new ValidationException("Задано пустое поле - владелец вещи");
        }

        if (!item.getOwner().getId().equals(userId)) {
            log.warn("Неверно указан владалец вещи.");
            throw new ValidationException("Неверно указан владалец вещи");
        }
    }

}
