package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserWithOnlyNameDtoTest {
    @Autowired
    private JacksonTester<UserWithOnlyNameDto> json;

    @Test
    void shouldTestBuilderUserWithOnlyNameDtoTest() throws IOException {
        //do
        UserWithOnlyNameDto dto = UserWithOnlyNameDto.builder().name("user").build();
        JsonContent<UserWithOnlyNameDto> result = json.write(dto);

        //check
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("user");
    }
}