package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserWithOnlyNameDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestWithItemDtoTest {
    @Autowired
    private JacksonTester<ItemRequestWithItemDto> json;

    @Test
    void shouldTestBuilderItemRequestWithItemDto() throws IOException {
        //prepare
        User user = UserMapper.mapToUser(UserDto.builder().id(1L).name("user").email("user@user.ru").build());
        UserWithOnlyNameDto requestor = UserWithOnlyNameDto.builder().name("user1").build();
        ItemForItemRequestDto itemDto = ItemForItemRequestDto.builder()
                .id(1L)
                .name("item")
                .owner(user)
                .build();
        List<ItemForItemRequestDto> items = new ArrayList<>();
        items.add(itemDto);

        //do
        ItemRequestWithItemDto dto = ItemRequestWithItemDto.builder()
                .id(1L)
                .description("itemRequest description")
                .items(items)
                .requestor(requestor)
                .created(Instant.parse("2025-01-01T00:00:00Z"))
                .build();

        JsonContent<ItemRequestWithItemDto> result = json.write(dto);

        //check
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("itemRequest description");
        assertThat(result).extractingJsonPathStringValue("$.requestor.name").isEqualTo("user1");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2025-01-01T00:00:00Z");
    }
}