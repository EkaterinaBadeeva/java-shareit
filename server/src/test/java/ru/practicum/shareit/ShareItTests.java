package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ShareItTests {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldPostUser() {
        UserDto expectedUserDto = UserDto.builder().id(1L).name("User Test").email("userTest@test.ru").build();

        UserDto user = UserDto.builder().id(2L).name("User Test").email("userTest@test.ru").build();

        UserDto actualUser = restTemplate.postForObject("/users", user, UserDto.class);
        assertThat(actualUser)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedUserDto);
    }
}