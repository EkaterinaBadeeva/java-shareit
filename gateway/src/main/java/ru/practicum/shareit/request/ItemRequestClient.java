package ru.practicum.shareit.request;

import ru.practicum.shareit.client.BaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    //POST /requests
    // добавление нового запроса на вещь
    public ResponseEntity<Object> create(ItemRequestDto itemRequestDto, Long userId) {
        return post("", userId, itemRequestDto);
    }

    //GET /requests
    // получение списка своих запросов вместе с данными об ответах на них
    public ResponseEntity<Object> getItemRequestsOfRequestor(Long userId) {
        return get("", userId);
    }

    //GET /requests/all
    // получение списка запросов, созданных другими пользователями
    public ResponseEntity<Object> getItemRequestsOfAllUsers(Long userId) {
        return get("/all", userId);
    }

    //GET /requests/{requestId}
    // получение данных об одном конкретном запросе вместе с данными об ответах на него
    public ResponseEntity<Object> getItemRequestById(Long requestId, Long userId) {
        return get("/" + requestId, userId);
    }

}
