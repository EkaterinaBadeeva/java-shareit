package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public Optional<User> getUserById(Long id) {
        User user = users.get(id);

        if (user == null) {
            return Optional.empty();
        } else {
            return Optional.of(user);
        }
    }

    public User create(User user) {
        // формируем дополнительные данные
        user.setId(getNextId());

        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    public User update(User newUser, Long id) {

        if (users.containsKey(id)) {
            User oldUser = users.get(id);

            // обновляем информацию о пользователе
            if (newUser.getName() != null) {
                oldUser.setName(newUser.getName());
            }

            if (newUser.getEmail() != null) {
                oldUser.setEmail(newUser.getEmail());
            }

            return oldUser;
        }
        throw new NotFoundException("Пользователь с email = " + newUser.getEmail() + " не найден");
    }

    public void deleteAllUsers() {
        users.clear();
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}