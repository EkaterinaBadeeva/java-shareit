package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemRequestServiceImplTest {
    @Autowired
    ItemRequestService itemRequestService;
    @Autowired
    UserService userService;

    UserDto userDto;
    User user;
    ItemRequestDto itemRequestDto;

    @BeforeEach
    void beforeEach() {
        userDto = UserDto.builder().id(1L).name("test1").email("test1@test.ru").build();
        user = UserMapper.mapToUser(userService.create(userDto));

        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Test description")
                .requestor(user)
                .created(Instant.now())
                .build();
    }

    @Test
    void shouldCreateItemRequest() {
        //do
        ItemRequestDto createdItemRequest = itemRequestService.create(itemRequestDto, user.getId());
        ItemRequestWithItemDto newItemRequest = itemRequestService.getItemRequestById(createdItemRequest.getId(), user.getId());

        //check
        assertNotNull(newItemRequest.getId());
        assertThat(newItemRequest.getId()).isEqualTo(createdItemRequest.getId());
        assertThat(newItemRequest.getDescription()).isEqualTo("Test description");
        assertThat(newItemRequest.getRequestor().getName()).isEqualTo("test1");
    }

    @Test
    void shouldGetItemRequestsOfRequestor() {
        //prepare
        ItemRequestDto createdItemRequest = itemRequestService.create(itemRequestDto, user.getId());
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(createdItemRequest);
        List<ItemForItemRequestDto> items = new ArrayList<>();
        ItemRequestWithItemDto itemRequestWithItem = ItemRequestMapper.mapToItemRequestWithItemDto(itemRequest, items);

        //do
        List<ItemRequestWithItemDto> foundItems = itemRequestService.getItemRequestsOfRequestor(user.getId());

        //check
        assertNotNull(foundItems);
        assertThat(foundItems).hasSize(1);
        assertThat(foundItems.getFirst().getId()).isEqualTo(itemRequestWithItem.getId());
        assertThat(foundItems.getFirst().getDescription()).isEqualTo("Test description");
        assertThat(foundItems.getFirst().getRequestor().getName()).isEqualTo("test1");
    }

    @Test
    void shouldGetItemRequestsOfAllUsers() {
        //prepare
        ItemRequestDto createdItemRequest = itemRequestService.create(itemRequestDto, user.getId());

        UserDto userDto1 = UserDto.builder().id(2L).name("test2").email("test2@test.ru").build();
        User user1 = UserMapper.mapToUser(userService.create(userDto1));

        ItemRequestDto itemRequestDto1 = ItemRequestDto.builder()
                .id(2L)
                .description("Test1 description")
                .requestor(user1)
                .created(Instant.now())
                .build();
        ItemRequestDto createdItemRequest1 = itemRequestService.create(itemRequestDto1, user1.getId());

        //do
        List<ItemRequestDto> foundItems = itemRequestService.getItemRequestsOfAllUsers(user.getId());

        //check
        assertNotNull(foundItems);
        assertThat(foundItems).hasSize(1);
        assertThat(foundItems.getFirst().getId()).isEqualTo(createdItemRequest1.getId());
        assertThat(foundItems.getFirst().getDescription()).isEqualTo("Test1 description");
        assertThat(foundItems.getFirst().getRequestor().getName()).isEqualTo("test2");
    }

    @Test
    void shouldGetItemRequestById() {
        //prepare
        ItemRequestDto createdItemRequest = itemRequestService.create(itemRequestDto, user.getId());

        //do
        ItemRequestWithItemDto newItemRequest = itemRequestService.getItemRequestById(createdItemRequest.getId(), user.getId());

        //check
        assertNotNull(newItemRequest.getId());
        assertNotNull(newItemRequest.getDescription());
        assertThat(newItemRequest.getId()).isEqualTo(createdItemRequest.getId());
        assertThat(newItemRequest.getDescription()).isEqualTo("Test description");
        assertThat(newItemRequest.getRequestor().getName()).isEqualTo("test1");
    }
}