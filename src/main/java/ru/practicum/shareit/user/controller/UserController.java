package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //GET /users
    // получить всех пользователей
    @GetMapping
    public Collection<UserDto> findAllUsers() {
        return userService.findAllUsers();
    }

    //GET /users/{id}
    // получить пользователя по id
    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    //POST /users
    // добавить пользователя
    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    //PATCH /users
    // обновить данные пользователя
    @PatchMapping("/{id}")
    public UserDto update(@Valid @RequestBody UserDto userDto,
                          @PathVariable Long id) {
        return userService.update(userDto, id);
    }

    //DELETE /users
    // удалить всех пользователей
    @DeleteMapping
    public Collection<UserDto> deleteAllUsers() {
        return userService.deleteAllUsers();
    }

    //DELETE /users/{id}
    // удалить пользователя по id
    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
