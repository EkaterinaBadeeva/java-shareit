package ru.practicum.shareit.user;

import ru.practicum.shareit.client.BaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    //GET /users
    // получить всех пользователей
    public ResponseEntity<Object> findAllUsers() {
        return get("");
    }

    //GET /users/{id}
    // получить пользователя по id
    public ResponseEntity<Object> findUserById(Long id) {
        return get("/" + id);
    }

    //POST /users
    // добавить пользователя
    public ResponseEntity<Object> create(UserDto userDto) {
        return post("", userDto);
    }

    //PATCH /users/{id}
    // обновить данные пользователя
    public ResponseEntity<Object> update(UserDto userDto, Long id) {
        return patch("/" + id, userDto);
    }

    //DELETE /users
    // удалить всех пользователей
    public ResponseEntity<Object> deleteAllUsers() {
        return delete("");
    }

    //DELETE /users/{id}
    // удалить пользователя по id
    public ResponseEntity<Object> deleteUserById(Long id) {
        return delete("/" + id);
    }
}
