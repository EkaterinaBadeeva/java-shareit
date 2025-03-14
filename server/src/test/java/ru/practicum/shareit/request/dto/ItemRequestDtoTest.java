package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void shouldTestBuilderItemRequestDto() throws IOException {
        //prepare
        User user = UserMapper.mapToUser(UserDto.builder().id(1L).name("user").email("user@user.ru").build());

        //do
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("itemRequest description")
                .requestor(user)
                .created(Instant.parse("2025-01-01T00:00:00Z"))
                .build();

        JsonContent<ItemRequestDto> result = json.write(dto);

        //check
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("itemRequest description");
        assertThat(result).extractingJsonPathStringValue("$.requestor.name").isEqualTo("user");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2025-01-01T00:00:00Z");
    }
}