package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.CommonException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceImplTest {
    @Autowired
    UserService userService;

    UserDto user1;
    UserDto user2;

    @BeforeEach
    void beforeEach() {
        user1 = UserDto.builder().name("test1").email("test1@test.ru").build();
        user2 = UserDto.builder().name("test2").email("test2@ytest.ru").build();
    }

    @Test
    void shouldFindAllUsers() {
        //prepare
        UserDto userCreated = userService.create(user1);

        //do
        List<UserDto> users = userService.findAllUsers().stream().toList();

        //check
        assertThat(users.getLast().getId()).isEqualTo(userCreated.getId());
        assertThat(users.getLast().getName()).isEqualTo(userCreated.getName());
        assertThat(users.getLast().getEmail()).isEqualTo(userCreated.getEmail());
    }

    @Test
    void shouldFindUserById() {
        //prepare
        UserDto createdUser = userService.create(user1);

        //do
        UserDto foundUser = userService.findUserById(createdUser.getId());

        //check
        assertThat(foundUser.getId()).isEqualTo(createdUser.getId());
        assertThat(foundUser.getName()).isEqualTo("test1");
        assertThat(foundUser.getEmail()).isEqualTo("test1@test.ru");
    }

    @Test
    void shouldThrowExceptionWhenNotFindUserById() {
        //prepare
        userService.create(user1);

        //check
        assertThatThrownBy(() -> userService.findUserById(2L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldCreateUser() {
        //do
        UserDto createdUser = userService.create(user1);
        UserDto newUser = userService.findUserById(createdUser.getId());

        //check
        assertNotNull(newUser.getId());
        assertThat(newUser.getId()).isEqualTo(createdUser.getId());
        assertThat(newUser.getName()).isEqualTo("test1");
        assertThat(newUser.getEmail()).isEqualTo("test1@test.ru");
    }

    @Test
    void shouldUpdateUser() {
        //prepare
        user1 = userService.create(user1);
        UserDto user = UserDto.builder().name("test").email("test@test.ru").build();

        //do
        user1 = userService.update(user,user1.getId());

        //check
        assertNotNull(user1.getId());
        assertThat(user1.getName()).isEqualTo("test");
        assertThat(user1.getEmail()).isEqualTo("test@test.ru");
    }

    @Test
    void shouldDeleteAllUsers() {
        //prepare
        user1 = userService.create(user1);
        user2 = userService.create(user2);

        //do
        userService.deleteAllUsers();

        //check
        assertThatThrownBy(() -> userService.findUserById(user1.getId()))
                .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> userService.findUserById(user2.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldDeleteUserById() {
        //prepare
        UserDto user = userService.create(user1);

        //do
        userService.deleteUserById(user.getId());

        //check
        assertThatThrownBy(() -> userService.findUserById(user.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenEmailOFUserIsExist() {
        //prepare
        user1 = userService.create(user1);
        user2 = userService.create(user2);
        UserDto user = UserDto.builder().name("test").email(user2.getEmail()).build();

        //check
        assertThatThrownBy(() -> userService.update(user,user1.getId()))
                .isInstanceOf(CommonException.class);
    }

    @Test
    void shouldThrowExceptionWhenIdNotFind() {
        //prepare
        userService.create(user1);

        //check
        assertThatThrownBy(() -> userService.deleteUserById(null))
                .isInstanceOf(ValidationException.class);
    }
}
