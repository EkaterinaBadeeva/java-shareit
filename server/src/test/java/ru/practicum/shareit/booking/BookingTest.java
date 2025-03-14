package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusOfBooking;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BookingTest {

    @Test
    void shouldTestEquals() {
        //prepare
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(LocalDateTime.of(2024, 1, 1, 11, 11));
        booking1.setEnd(LocalDateTime.of(2024, 2, 2, 22, 22));
        booking1.setStatus(StatusOfBooking.APPROVED);

        Booking booking2 = new Booking();
        booking2.setId(1L);
        booking2.setStart(LocalDateTime.of(2024, 1, 1, 11, 11));
        booking2.setEnd(LocalDateTime.of(2024, 2, 2, 22, 22));
        booking2.setStatus(StatusOfBooking.APPROVED);

        Booking booking3 = new Booking();
        booking3.setId(3L);
        booking3.setStart(LocalDateTime.of(2024, 1, 1, 11, 11));
        booking3.setEnd(LocalDateTime.of(2024, 2, 2, 22, 22));
        booking3.setStatus(StatusOfBooking.APPROVED);

        //check
        assertEquals(booking1, booking2);
        assertNotEquals(booking1, booking3);
    }

    @Test
    void shouldTestHashCode() {
        //prepare
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(LocalDateTime.of(2024, 1, 1, 11, 11));
        booking1.setEnd(LocalDateTime.of(2024, 2, 2, 22, 22));
        booking1.setStatus(StatusOfBooking.APPROVED);

        Booking booking2 = new Booking();
        booking2.setId(1L);
        booking2.setStart(LocalDateTime.of(2024, 1, 1, 11, 11));
        booking2.setEnd(LocalDateTime.of(2024, 2, 2, 22, 22));
        booking2.setStatus(StatusOfBooking.APPROVED);

        //check
        assertEquals(booking1.hashCode(), booking2.hashCode());
    }
}