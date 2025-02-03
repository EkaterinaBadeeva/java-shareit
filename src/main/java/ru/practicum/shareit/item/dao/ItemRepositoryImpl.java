package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Long>> idItemsOfUser = new HashMap<>();

    @Override
    public Collection<Item> getAllItems() {
        return items.values();
    }

    @Override
    public Collection<Item> getAllItemsOfUser(Long userId) {
        Collection<Item> itemsUser = new ArrayList<>();

        if (!idItemsOfUser.containsKey(userId)) {
            return List.of();
        } else {
            List<Long> idItems = idItemsOfUser.get(userId);
            for (Long id : idItems) {
                itemsUser.add(items.get(id));
            }
        }

        return itemsUser;
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        Item item = items.get(id);

        if (item == null) {
            return Optional.empty();
        } else {
            return Optional.of(item);
        }
    }

    @Override
    public Item create(Item item, Long userId) {
        // формируем дополнительные данные
        item.setId(getNextId());

        // сохраняем новую вещь пользователя в памяти приложения
        items.put(item.getId(), item);
        List<Long> idItems = idItemsOfUser.get(userId);

        if (idItems == null) {
            idItems = new ArrayList<>();
            idItemsOfUser.put(userId, idItems);
        }
        idItems.add(item.getId());

        return item;
    }

    @Override
    public Item update(Item oldItem, Map<String, String> fields) {

        if (items.containsKey(oldItem.getId())) {

            // обновляем информацию о вещи
            String newName = fields.get("name");
            String newDescription = fields.get("description");
            String newAvailable = fields.get("available");

            if (newName != null) {
                oldItem.setName(newName);
            }

            if (newDescription != null) {
                oldItem.setDescription(newDescription);
            }

            if (newAvailable != null) {
                oldItem.setAvailable(Boolean.valueOf(newAvailable));
            }

            return oldItem;
        }
        throw new NotFoundException("Вещь с владельцем = " + oldItem.getOwner().getName() + " не найдена");
    }

    // вспомогательный метод для генерации идентификатора новой вещи
    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
