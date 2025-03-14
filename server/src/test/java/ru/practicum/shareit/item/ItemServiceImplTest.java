package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.StatusOfBooking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemServiceImplTest {
    @Autowired
    ItemService itemService;
    @Autowired
    BookingService bookingService;
    @Autowired
    UserService userService;

    UserDto userDto;
    User user;
    ItemWithRequestDto itemWithRequestDto;

    @BeforeEach
    void beforeEach() {
        userDto = UserDto.builder().id(1L).name("test1").email("test1@test.ru").build();
        user = UserMapper.mapToUser(userService.create(userDto));

        itemWithRequestDto = ItemWithRequestDto.builder()
                .name("item test")
                .description("item test description")
                .available(true)
                .build();
    }

    @Test
    void shouldGetAllItemsOfUser() {
        //prepare
        ItemDto createdItem = itemService.create(itemWithRequestDto, user.getId());

        //do
        Collection<ItemDto> foundItems = itemService.getAllItemsOfUser(user.getId());

        //check
        assertNotNull(foundItems);
        assertThat(foundItems).hasSize(1);
        assertTrue(foundItems.contains(createdItem));
    }

    @Test
    void shouldGetItemById() {
        //prepare
        ItemDto createdItem = itemService.create(itemWithRequestDto, user.getId());

        //do
        ItemDto foundItem = itemService.getItemById(createdItem.getId(), user.getId());

        //check
        assertNotNull(foundItem.getId());
        assertThat(foundItem.getId()).isEqualTo(createdItem.getId());
        assertThat(foundItem.getDescription()).isEqualTo("item test description");
        assertThat(foundItem.getAvailable()).isEqualTo(true);
    }

    @Test
    void shouldCreateItem() {
        //do
        ItemDto createdItem = itemService.create(itemWithRequestDto, user.getId());
        ItemDto newItem = itemService.getItemById(createdItem.getId(), user.getId());

        //check
        assertNotNull(newItem.getId());
        assertThat(newItem.getId()).isEqualTo(createdItem.getId());
        assertThat(newItem.getDescription()).isEqualTo("item test description");
        assertThat(newItem.getAvailable()).isEqualTo(true);
    }

    @Test
    void shouldUpdateItem() {
        //prepare
        ItemDto createdItem = itemService.create(itemWithRequestDto, user.getId());
        Map<String, String> fields = new HashMap<>();
        fields.put("name", "name");
        fields.put("description", "description");
        fields.put("available", "true");

        //do
        ItemDto updatedItem = itemService.update(fields, createdItem.getId(), user.getId());

        //check
        assertNotNull(updatedItem.getId());
        assertThat(updatedItem.getId()).isEqualTo(createdItem.getId());
        assertThat(updatedItem.getName()).isEqualTo("name");
        assertThat(updatedItem.getDescription()).isEqualTo("description");
        assertThat(updatedItem.getAvailable()).isEqualTo(true);
    }

    @Test
    void shouldSearchItem() {
        //prepare
        ItemDto createdItem = itemService.create(itemWithRequestDto, user.getId());
        String text = "item";

        //do
        Collection<ItemDto> updatedItems = itemService.searchItem(text);

        //check
        assertNotNull(updatedItems);
        assertThat(updatedItems).hasSize(1);
        assertTrue(updatedItems.contains(createdItem));
    }

    @Test
    void shouldCreateComment() {
        //prepare
        ItemDto createdItem = itemService.create(itemWithRequestDto, user.getId());
        Item item = ItemMapper.mapToItem(createdItem);
        UserDto userDto2 = UserDto.builder().id(2L).name("test2").email("test2@test.ru").build();
        User user2 = UserMapper.mapToUser(userService.create(userDto2));

        BookingDtoShort bookingDtoShort = BookingDtoShort.builder()
                .start(LocalDateTime.of(2024, 1, 1, 11, 11))
                .end(LocalDateTime.of(2024, 2, 2, 22, 24))
                .itemId(item.getId())
                .status(StatusOfBooking.WAITING)
                .build();
        BookingDto createdBookingDto = bookingService.create(bookingDtoShort, user2.getId());
        BookingDto approvedBookingDto = bookingService.approved(true, createdBookingDto.getId(), user.getId());
        Item i = ItemMapper.mapToItem(itemService.getItemById(item.getId(), user.getId()));

        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("comment")
                .item(i)
                .authorName(user2.getName())
                .build();
        //do
        CommentDto commentCreate = itemService.createComment(commentDto, i.getId(), user2.getId());

        //check
        assertNotNull(commentCreate.getId());
        assertThat(commentCreate.getId()).isEqualTo(1);
        assertThat(commentCreate.getText()).isEqualTo("comment");
        assertThat(commentCreate.getItem().getName()).isEqualTo("item test");
    }

    @Test
    void shouldThrowExceptionWhenNotGetItemById() {
        //prepare
        itemService.create(itemWithRequestDto, user.getId());

        //check
        assertThatThrownBy(() -> itemService.getItemById(1000L, user.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenUpdateItemNotOwner() {
        //prepare
        ItemDto createdItem = itemService.create(itemWithRequestDto, user.getId());
        Map<String, String> fields = new HashMap<>();
        fields.put("name", "name");
        fields.put("description", "description");
        fields.put("available", "true");

        UserDto userDto2 = UserDto.builder().id(2L).name("test2").email("test2@test.ru").build();
        User user2 = UserMapper.mapToUser(userService.create(userDto2));
        //check
        assertThatThrownBy(() -> itemService.update(fields, createdItem.getId(), user2.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenIdNotFind() {
        //prepare
        itemService.create(itemWithRequestDto, user.getId());

        //check
        assertThatThrownBy(() -> itemService.getItemById(null,user.getId()))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFind() {
        //prepare
        ItemDto createdItem = itemService.create(itemWithRequestDto, user.getId());

        //check
        assertThatThrownBy(() -> itemService.getItemById(createdItem.getId(),-1L))
                .isInstanceOf(NotFoundException.class);
    }
}