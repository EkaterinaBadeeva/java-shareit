package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.CommonException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Collection<UserDto> findAllUsers() {
        log.info("Получение списка всех пользователей.");
        return userRepository.getAllUsers().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto findUserById(Long id) {
        log.info("Получение пользователя по Id.");
        return userRepository.getUserById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
    }

    public UserDto create(UserDto userDto) {
        log.info("Добавление пользователя.");
        User user = UserMapper.mapToUser(userDto);
        checkConditions(user);
        checkEmail(user);
        user = userRepository.create(user);

        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UserDto userDto, Long id) {
        log.info("Обновление пользователя.");
        User user = UserMapper.mapToUser(userDto);
        checkId(id);
        checkEmail(user);
        if (userRepository.getUserById(id).isEmpty()) {
            throw new NotFoundException("Пользователь с email = " + user.getEmail() + " не найден");
        }
        user = userRepository.update(user, id);

        return UserMapper.mapToUserDto(user);
    }

    public Collection<UserDto> deleteAllUsers() {
        log.info("Удаление всех пользователей.");
        userRepository.deleteAllUsers();
        return findAllUsers();
    }

    public UserDto deleteUser(Long id) {
        log.info("Удаление пользователя.");

        checkId(id);

        userRepository.deleteUser(id);

        return userRepository.getUserById(id)
                .map(UserMapper::mapToUserDto).orElse(null);
    }


    private void checkId(Long id) {
        if (id == null) {
            log.warn("Id должен быть указан.");
            throw new ValidationException("Id должен быть указан");
        }
    }

    private void checkEmail(User user) {
        for (User us : userRepository.getAllUsers()) {
            if (us.getEmail().equals(user.getEmail())) {
                log.warn("Пользователь уже существует.");
                throw new CommonException("Пользователь с email = " + user.getEmail() + " уже существует");
            }
        }
    }

    private void checkConditions(User user) {

        if (user.getName().isEmpty()) {
            log.warn("Задано пустое имя пользователя.");
            throw new ValidationException("Задано пустое имя пользователя");
        }

        if (user.getEmail().isEmpty()) {
            log.warn("Задан пустой email пользователя.");
            throw new ValidationException("Задан пустой email пользователя");
        }
    }
}
