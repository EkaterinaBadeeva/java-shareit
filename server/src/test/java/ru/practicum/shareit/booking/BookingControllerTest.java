package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.StatusOfBooking;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookingService bookingService;

    BookingDtoShort booking1;
    BookingDto bookingDto;

    @BeforeEach
    void beforeEach() {

        booking1 = BookingDtoShort.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(10))
                .itemId(1L)
                .status(StatusOfBooking.WAITING)
                .build();

        bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(10))
                .status(StatusOfBooking.WAITING)
                .build();
    }

    @Test
    void shouldCreateBooking() throws Exception {
        //prepare
        Long id = 1L;
        bookingDto.setId(id);

        //do
        when(bookingService.create(booking1, 1L)).thenReturn(bookingDto);

        //check
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking1))

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void shouldApproved() throws Exception {
        //prepare
        Long id = 1L;
        bookingDto.setId(id);
        bookingDto.setStatus(StatusOfBooking.APPROVED);

        //do
        when(bookingService.approved(true, 1L, 1L)).thenReturn(bookingDto);

        //check
        mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking1))

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"));

    }

    @Test
    void shouldGetBookingById() throws Exception {
        //prepare
        Long id = 1L;
        bookingDto.setId(id);

        //do
        when(bookingService.getBookingById(1L, 1L)).thenReturn(bookingDto);

        //check
        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void shouldFindBookingByBooker() throws Exception {
        //prepare
        Long id = 1L;
        bookingDto.setId(id);
        List<BookingDto> bookings = List.of(bookingDto);

        //do
        when(bookingService.findBookingByBooker(State.ALL, 1L)).thenReturn(bookings);

        //check
        mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"));
    }

    @Test
    void shouldFindBookingByOwner() throws Exception {
        //prepare
        Long id = 1L;
        bookingDto.setId(id);
        List<BookingDto> bookings = List.of(bookingDto);

        //do
        when(bookingService.findBookingByOwner(State.ALL, 1L)).thenReturn(bookings);

        //check
        mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"));
    }
}