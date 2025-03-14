package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
@JsonTest
class ItemForItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemForItemRequestDto> json;

    @Test
    void shouldTestBuilderItemForItemRequestDto() throws IOException {
        //prepare
        User user = UserMapper.mapToUser(UserDto.builder().id(1L).name("user").email("user@user.ru").build());

        //do
        ItemForItemRequestDto dto = ItemForItemRequestDto.builder()
                .id(1L)
                .name("item")
                .owner(user)
                .build();

        JsonContent<ItemForItemRequestDto> result = json.write(dto);

        //check
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("user");
    }
}