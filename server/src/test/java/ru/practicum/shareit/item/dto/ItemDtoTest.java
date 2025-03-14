package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.StatusOfBooking;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void shouldTestBuilderItemDto() throws IOException {
        //prepare
        User user = UserMapper.mapToUser(UserDto.builder().id(1L).name("user").email("user@user.ru").build());
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("comment")
                .authorName(user.getName())
                .created(Instant.parse("2025-01-01T00:00:00Z"))
                .build();
        List<CommentDto> comments = new ArrayList<>();
        comments.add(commentDto);

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("itemRequest description")
                .requestor(user)
                .build();
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto);

        BookingDtoShort lastBooking = BookingDtoShort.builder()
                .start(LocalDateTime.of(2000, 2, 2, 22, 22))
                .end(LocalDateTime.of(2024, 2, 2, 22, 24))
                .itemId(1L)
                .status(StatusOfBooking.APPROVED)
                .build();
        BookingDtoShort nextBooking = BookingDtoShort.builder()
                .start(LocalDateTime.of(2222, 2, 2, 22, 22))
                .end(LocalDateTime.of(2224, 2, 2, 22, 24))
                .itemId(1L)
                .status(StatusOfBooking.APPROVED)
                .build();

        //do
        ItemDto dto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("item description")
                .available(true)
                .owner(user)
                .request(itemRequest)
                .comments(comments)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();
        JsonContent<ItemDto> result = json.write(dto);

        //check
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("item description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("user");
        assertThat(result).extractingJsonPathNumberValue("$.request.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.request.description")
                .isEqualTo("itemRequest description");
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.start").isEqualTo("2000-02-02T22:22:00");
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.start").isEqualTo("2222-02-02T22:22:00");
    }
}