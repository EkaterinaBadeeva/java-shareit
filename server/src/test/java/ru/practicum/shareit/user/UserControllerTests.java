package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    UserDto user1;
    UserDto user2;

    @BeforeEach
    void beforeEach() {
        user1 = UserDto.builder().name("test1").email("test1@test.ru").build();
        user2 = UserDto.builder().name("test2").email("test2@ytest.ru").build();
    }

    @Test
    void shouldFindAllUsers() throws Exception {
        //prepare
        user1.setId(1L);
        List<UserDto> users = List.of(user2, user1);

        //do
        when(userService.findAllUsers()).thenReturn(users);

        //check
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[1].name").value("test1"))
                .andExpect(jsonPath("$[1].email").value("test1@test.ru"));

    }

    @Test
    void shouldFindUserById() throws Exception {
        //prepare
        Long id = 1L;
        user1.setId(id);

        //do
        when(userService.findUserById(id)).thenReturn(user1);

        //check
        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test1"))
                .andExpect(jsonPath("$.email").value("test1@test.ru"));

    }

    @Test
    void shouldCreateUser() throws Exception {
        //prepare
        Long id = 1L;
        user1.setId(id);

        //do
        when(userService.create(user1)).thenReturn(user1);

        //check
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1))

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test1"))
                .andExpect(jsonPath("$.email").value("test1@test.ru"));

    }

    @Test
    void shouldUpdateUser() throws Exception {
        //prepare
        UserDto user = UserDto.builder().name("test").email("test@test.ru").build();
        Long id = 1L;
        user1.setId(id);

        //do
        when(userService.update(user, id)).thenReturn(user1);

        //check
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test1"))
                .andExpect(jsonPath("$.email").value("test1@test.ru"));

    }

    @Test
    void shouldDeleteAllUsers() throws Exception {
        //prepare
        user1.setId(1L);
        user2.setId(2L);

        //do
        doNothing().when(userService).deleteAllUsers();

        //check
        mockMvc.perform(delete("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        //prepare
        Long id = 1L;
        user1.setId(id);

        //do
        doNothing().when(userService).deleteUserById(1L);

        //check
        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
