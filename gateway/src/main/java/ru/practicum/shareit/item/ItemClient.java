package ru.practicum.shareit.item;

import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    //GET /items
    // просмотр владельцем списка всех его вещей
    public ResponseEntity<Object> getAllItemsOfUser(Long userId) {
        return get("", userId);
    }

    //GET /items/{itemId}
    // найти вещь по Id
    public ResponseEntity<Object> getItemById(Long id, Long userId) {
        return get("/" + id, userId);
    }

    //POST /items
    // добавить вещь
    public ResponseEntity<Object> create(ItemWithRequestDto itemDto, Long userId) {
        return post("", userId, itemDto);
    }

    //PATCH /items/{itemId}
    // отредактировать вещь
    public ResponseEntity<Object> update(Map<String, String> fields, Long itemId, Long userId) {
        return post("/" + itemId, userId, fields);
    }

    //GET /items/search?text={text}
    // поиск вещи потенциальным арендатором
    public ResponseEntity<Object> searchItem(String text) {
        return get("/search?text=" + text);
    }

    //POST /items/{itemId}/comment
    // добавление комментария о вещи после аренды
    public ResponseEntity<Object> createComment(CommentDto commentDto, Long itemId, Long userId) {
        return post("/" + itemId + "/comment", userId,null, commentDto);
    }
}
