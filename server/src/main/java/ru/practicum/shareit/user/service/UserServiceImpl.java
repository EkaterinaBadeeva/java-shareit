package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.CommonException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dao.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> findAllUsers() {
        log.info("Получение списка всех пользователей.");
        return userRepository.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(Long id) {
        log.info("Получение пользователя по Id.");
        return userRepository.findById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Id пользователя указан не верно. " +
                        "Пользователь с id " + id + " не найден"));
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        log.info("Добавление пользователя.");
        User user = UserMapper.mapToUser(userDto);
        checkConditions(user);
        checkEmail(user);
        user = userRepository.save(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, Long id) {
        log.info("Обновление пользователя.");
        User user = UserMapper.mapToUser(userDto);
        checkId(id);

        Optional<User> oldUserOpt = userRepository.findById(id);

        if (oldUserOpt.isEmpty()) {
            throw new NotFoundException("Пользователь с email = " + user.getEmail() + " не найден");
        }

        String newEmail = user.getEmail();

        User oldUser = oldUserOpt.get();

        if (newEmail != null) {
            String oldEmail = oldUser.getEmail();

            if (oldEmail != null && !user.getEmail().equals(oldEmail)) {
                checkEmail(user);
            }
        }

        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }

        return UserMapper.mapToUserDto(oldUser);
    }

    @Override
    @Transactional
    public void deleteAllUsers() {
        log.info("Удаление всех пользователей.");
        userRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        log.info("Удаление пользователя.");

        checkId(id);

        userRepository.deleteById(id);
    }

    private void checkId(Long id) {
        if (id == null) {
            log.warn("Id должен быть указан.");
            throw new ValidationException("Id должен быть указан");
        }
    }

    private void checkEmail(User user) {
        for (User us : userRepository.findAll()) {
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
