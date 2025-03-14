package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUsers();

    UserDto findUserById(Long id);

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, Long id);

    void deleteAllUsers();

    void deleteUserById(Long id);
}
