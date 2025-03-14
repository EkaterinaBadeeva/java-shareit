package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void shouldTestBuilderCommentDto() throws IOException {
        //prepare
        User user = UserMapper.mapToUser(UserDto.builder().id(1L).name("user").email("user@user.ru").build());
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("item description")
                .available(true)
                .owner(user)
                .build();
        Item item = ItemMapper.mapToItem(itemDto);

        //do
        CommentDto dto = CommentDto.builder()
                .id(1L)
                .text("comment")
                .item(item)
                .authorName(user.getName())
                .created(Instant.parse("2025-01-01T00:00:00Z"))
                .build();

        JsonContent<CommentDto> result = json.write(dto);

        //check
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("comment");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo("item description");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("user");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2025-01-01T00:00:00Z");
    }
}