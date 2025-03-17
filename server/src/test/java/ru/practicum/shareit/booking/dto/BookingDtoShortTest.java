package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.StatusOfBooking;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoShortTest {
    @Autowired
    private JacksonTester<BookingDtoShort> json;

    @Test
    void shouldTestBuilderBookingDtoShort() throws IOException {
        //do
        BookingDtoShort dto = BookingDtoShort.builder()
                .start(LocalDateTime.of(2222, 2, 2, 22, 22))
                .end(LocalDateTime.of(2224, 2, 2, 22, 24))
                .itemId(1L)
                .status(StatusOfBooking.WAITING)
                .build();

        JsonContent<BookingDtoShort> result = json.write(dto);

        //check
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2222-02-02T22:22:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2224-02-02T22:24:00");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }
}