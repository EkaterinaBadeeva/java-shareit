package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserWithOnlyNameDto;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ItemRequestService itemRequestService;

    ItemRequestDto itemRequestDto1;
    ItemRequestDto itemRequestDto2;
    ItemRequestWithItemDto itemRequestWithItemDto;
    UserWithOnlyNameDto requestor;

    @BeforeEach
    void beforeEach() {
        requestor = UserWithOnlyNameDto.builder()
                .name("name").build();
        itemRequestDto1 = ItemRequestDto.builder()
                .id(1L)
                .description("Test description1")
                .created(Instant.now())
                .build();
        itemRequestDto2 = ItemRequestDto.builder()
                .id(2L)
                .description("Test description2")
                .created(Instant.now())
                .build();

        itemRequestWithItemDto = ItemRequestWithItemDto.builder()
                .id(1L)
                .description("Test description3")
                .requestor(requestor)
                .created(Instant.now())
                .build();
    }

    @Test
    void shouldCreateItemRequest() throws Exception {
        //prepare
        Long id = 1L;
        itemRequestDto2.setId(id);

        //do
        when(itemRequestService.create(itemRequestDto1, 1L)).thenReturn(itemRequestDto2);

        //check
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto1))

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Test description2"));
    }

    @Test
    void shouldGetItemRequestsOfRequestor() throws Exception {
        //prepare
        Long id = 1L;
        itemRequestDto1.setId(id);
        List<ItemRequestWithItemDto> itemRequests = List.of(itemRequestWithItemDto);

        //do
        when(itemRequestService.getItemRequestsOfRequestor(1L)).thenReturn(itemRequests);

        //check
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Test description3"));
    }

    @Test
    void shouldGetItemRequestsOfAllUsers() throws Exception {
        //prepare
        Long id = 1L;
        itemRequestDto1.setId(id);
        List<ItemRequestDto> itemRequests = List.of(itemRequestDto1);

        //do
        when(itemRequestService.getItemRequestsOfAllUsers(1L)).thenReturn(itemRequests);

        //check
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Test description1"));
    }

    @Test
    void shouldGetItemRequestById() throws Exception {
        //do
        when(itemRequestService.getItemRequestById(1L, 1L)).thenReturn(itemRequestWithItemDto);

        //check
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Test description3"));
    }
}