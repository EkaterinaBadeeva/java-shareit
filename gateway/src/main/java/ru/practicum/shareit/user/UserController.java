package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    //GET /users
    // получить всех пользователей
    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        log.info("Получение всех пользователей");
        return userClient.findAllUsers();
    }

    //GET /users/{id}
    // получить пользователя по id
    @GetMapping("/{id}")
    public ResponseEntity<Object> findUserById(@PathVariable Long id) {
     log.info("Получение пользователя по id: userId={}", id);
        return userClient.findUserById(id);
    }

    //POST /users
    // добавить пользователя
    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
    log.info("Добавление пользователя: userId={}", userDto);
        return userClient.create(userDto);
    }

    //PATCH /users/{id}
    // обновить данные пользователя
    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody UserDto userDto,
                          @PathVariable Long id) {
    log.info("Обновление данных пользователя: user={}, userId={}", userDto, id);
        return userClient.update(userDto, id);
    }

    //DELETE /users
    // удалить всех пользователей
    @DeleteMapping
    public ResponseEntity<Object> deleteAllUsers() {
    log.info("Удаление всех пользователей");
        return userClient.deleteAllUsers();
    }

    //DELETE /users/{id}
    // удалить пользователя по id
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        log.info("Удаление пользователя по id: userId={}", id);
       return userClient.deleteUserById(id);
    }
}
