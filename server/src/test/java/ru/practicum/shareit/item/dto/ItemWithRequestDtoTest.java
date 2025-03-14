package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemWithRequestDtoTest {
    @Autowired
    private JacksonTester<ItemWithRequestDto> json;

    @Test
    void shouldTestBuilderItemWithRequestDto() throws IOException {
        //do
        ItemWithRequestDto dto = ItemWithRequestDto.builder()
                .name("item")
                .description("item description")
                .available(true)
                .requestId(1L)
                .build();
        JsonContent<ItemWithRequestDto> result = json.write(dto);

        //check
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("item description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }
}