package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusOfBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Autowired
    private JacksonTester<Booking> json1;

    @Test
    void shouldTestBuilderBookingDto() throws IOException {
        //prepare
        User user = UserMapper.mapToUser(UserDto.builder().id(1L).name("user").email("user@user.ru").build());
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("item description")
                .available(true)
                .owner(user)
                .build();
        Item item = ItemMapper.mapToItem(itemDto);

        //do
        BookingDto dto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2222, 2, 2, 22, 22))
                .end(LocalDateTime.of(2224, 2, 2, 22, 24))
                .item(item)
                .booker(user)
                .status(StatusOfBooking.WAITING)
                .build();

        JsonContent<BookingDto> result = json.write(dto);

        //check
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2222-02-02T22:22:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2224-02-02T22:24:00");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo("item description");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("user");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }

    @Test
    void shouldMapToBooking() throws IOException {

        User user = UserMapper.mapToUser(UserDto.builder().id(1L).name("user").email("user@user.ru").build());
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("item description")
                .available(true)
                .owner(user)
                .build();
        Item item = ItemMapper.mapToItem(itemDto);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.of(2024, 1, 1, 11,11));
        booking.setEnd(LocalDateTime.of(2024, 2, 2, 22,22));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(StatusOfBooking.APPROVED);

        JsonContent<Booking> result = json1.write(booking);

        BookingDto dto2 = BookingDto.builder()
                .id(2L)
                .start(LocalDateTime.of(2024, 1, 1, 11,11))
                .end(LocalDateTime.of(2024, 2, 2, 22,22))
                .item(item)
                .booker(user)
                .status(StatusOfBooking.APPROVED)
                .build();
        Booking booking3 = BookingMapper.mapToBooking(dto2);
        JsonContent<Booking> result2 = json1.write(booking3);

        //check
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2024-01-01T11:11:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2024-02-02T22:22:00");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo("item description");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("user");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");

        //check
        assertThat(result2).extractingJsonPathStringValue("$.start").isEqualTo("2024-01-01T11:11:00");
        assertThat(result2).extractingJsonPathStringValue("$.end").isEqualTo("2024-02-02T22:22:00");
        assertThat(result2).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result2).extractingJsonPathStringValue("$.item.name").isEqualTo("item");
        assertThat(result2).extractingJsonPathStringValue("$.item.description")
                .isEqualTo("item description");
        assertThat(result2).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result2).extractingJsonPathStringValue("$.booker.name").isEqualTo("user");
        assertThat(result2).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
    }
}