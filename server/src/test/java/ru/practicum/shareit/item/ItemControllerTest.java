package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ItemService itemService;

    ItemDto itemDto1;
    ItemWithRequestDto itemDto2;
    ItemDto itemDto3;
    User owner;

    @BeforeEach
    void beforeEach() {
        owner = UserMapper.mapToUser(UserDto.builder()
                .id(1L)
                .name("name")
                .email("name@yandex.ru")
                .build());
        itemDto1 = ItemDto.builder()
                .id(1L)
                .name("Item1")
                .description("Description1")
                .available(true)
                .owner(owner)
                .build();
        itemDto2 = ItemWithRequestDto.builder()
                .name("Item2")
                .description("Description2")
                .available(true)
                .requestId(1L)
                .build();
        itemDto3 = ItemDto.builder()
                .id(3L)
                .name("Item3")
                .description("Description3")
                .available(true)
                .owner(owner)
                .build();
    }

    @Test
    void shouldGetAllItemsOfUser() throws Exception {
        //prepare
        Long id = 1L;
        itemDto1.setId(id);
        List<ItemDto> items = List.of(itemDto1);

        //do
        when(itemService.getAllItemsOfUser(1L)).thenReturn(items);

        //check
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Item1"))
                .andExpect(jsonPath("$[0].description").value("Description1"));
    }

    @Test
    void shouldGetItemById() throws Exception {
        //prepare
        Long id = 1L;
        itemDto1.setId(id);

        //do
        when(itemService.getItemById(1L, 1L)).thenReturn(itemDto1);

        //check
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Item1"))
                .andExpect(jsonPath("$.description").value("Description1"));
    }

    @Test
    void shouldCreate() throws Exception {
        //prepare
        Long id = 1L;
        itemDto1.setId(id);

        //do
        when(itemService.create(itemDto2, 1L)).thenReturn(itemDto1);

        //check
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto2))

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Description1"));
    }

    @Test
    void shouldUpdate() throws Exception {
        //prepare
        itemDto3.setId(1L);
        Map<String, String> fields = new HashMap<>();
        fields.put("name", "name");
        fields.put("description", "description");
        fields.put("available", "true");

        //do
        when(itemService.update(fields, 1L, 1L)).thenReturn(itemDto3);

        //check
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fields))

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Description3"));
    }

    @Test
    void shouldSearchItem() throws Exception {
        //prepare
        Long id = 1L;
        itemDto1.setId(id);

        //do
        when(itemService.searchItem("item")).thenReturn(List.of(itemDto1));

        mockMvc.perform(get("/items/search")
                        .param("text", "item")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void shouldCreateComment() throws Exception {
        //prepare
        CommentDto commentDto1 = CommentDto.builder()
                .id(1L)
                .text("comment")
                .created(Instant.parse("2025-01-01T00:00:00Z"))
                .build();
        CommentDto commentDto2 = CommentDto.builder()
                .id(2L)
                .text("comment2")
                .created(Instant.parse("2025-02-01T00:00:00Z"))
                .build();
        commentDto2.setId(1L);

        //do
        when(itemService.createComment(commentDto1, 1L, 1L)).thenReturn(commentDto2);

        //check
        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto1))

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("comment2"));
    }
}