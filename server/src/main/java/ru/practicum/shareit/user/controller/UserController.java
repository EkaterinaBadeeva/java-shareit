package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //GET /users
    // получить всех пользователей
    @GetMapping
    public List<UserDto> findAllUsers() {
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
    public UserDto create(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    //PATCH /users
    // обновить данные пользователя
    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto,
                          @PathVariable Long id) {
        return userService.update(userDto, id);
    }

    //DELETE /users
    // удалить всех пользователей
    @DeleteMapping
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    //DELETE /users/{id}
    // удалить пользователя по id
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
