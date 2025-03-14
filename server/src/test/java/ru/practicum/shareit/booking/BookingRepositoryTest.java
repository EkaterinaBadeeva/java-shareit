package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusOfBooking;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;

    UserDto userDto1;
    UserDto userDto2;
    ItemDto itemDto;
    BookingDto bookingDto;
    User user1 = new User();
    User user2 = new User();
    Item item = new Item();
    Booking booking = new Booking();

    @BeforeEach
    void beforeEach() {
        userDto1 = UserDto.builder().name("test1").email("test1@test.ru").build();
        userDto2 = UserDto.builder().name("test2").email("test2@test.ru").build();

        user1.setName(userDto1.getName());
        user1.setEmail(userDto1.getEmail());
        userRepository.save(user1);

        user2.setName(userDto2.getName());
        user2.setEmail(userDto2.getEmail());
        userRepository.save(user2);

        itemDto = ItemDto.builder()
                .name("item test")
                .description("item test description")
                .available(true)
                .owner(user2)
                .build();

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());
        item.setOwner(itemDto.getOwner());
        itemRepository.save(item);

        bookingDto = BookingDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .item(item)
                .booker(user1)
                .status(StatusOfBooking.WAITING)
                .build();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(bookingDto.getItem());
        booking.setBooker(bookingDto.getBooker());
        booking.setStatus(bookingDto.getStatus());
        bookingRepository.save(booking);
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldFindAllBookingsByBookerIdOrderByEndDesc() {
        //do
        List<Booking> bookings = bookingRepository.findAllBookingsByBookerIdOrderByEndDesc(user1.getId());

        //check
        assertNotNull(booking.getId());
        assertThat(bookings.getLast().getId()).isEqualTo(booking.getId());
        assertThat(bookings.getLast().getBooker().getId()).isEqualTo(booking.getBooker().getId());
    }

    @Test
    void shouldFindAllBookingsByBookerIdAndEndIsAfterOrderByEndDesc() {
        //do
        List<Booking> bookings = bookingRepository.findAllBookingsByBookerIdAndEndIsAfterOrderByEndDesc(booking.getBooker().getId(), LocalDateTime.now());

        //check
        assertFalse(bookings.isEmpty());
        assertNotNull(bookings.getLast().getId());
        assertThat(bookings.getLast().getId()).isEqualTo(booking.getId());
        assertThat(bookings.getLast().getBooker().getId()).isEqualTo(booking.getBooker().getId());
    }

    @Test
    void shouldFindAllBookingsByItemOwnerIdOrderByEndDesc() {
        //do
        List<Booking> bookings = bookingRepository.findAllBookingsByItemOwnerIdOrderByEndDesc(item.getOwner().getId());

        //check
        assertNotNull(booking.getId());
        assertThat(bookings.getLast().getId()).isEqualTo(booking.getId());
        assertThat(bookings.getLast().getBooker().getId()).isEqualTo(booking.getBooker().getId());
    }

    @Test
    void shouldFindBookingsByItemId() {
        //do
        List<Booking> bookings = bookingRepository.findBookingsByItemId(item.getId());

        //check
        assertNotNull(booking.getId());
        assertThat(bookings.getLast().getId()).isEqualTo(booking.getId());
        assertThat(bookings.getLast().getBooker().getId()).isEqualTo(booking.getBooker().getId());
        assertThat(bookings.getLast().getItem().getId()).isEqualTo(item.getId());
    }

    @Test
    void shouldFindBookingsByItemInAndStatusOrderByStartDesc() {
        //prepare
        BookingDto bookingDto2 = BookingDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .item(item)
                .booker(user1)
                .status(StatusOfBooking.APPROVED)
                .build();

        Booking booking2 = new Booking();
        booking2.setStart(bookingDto2.getStart());
        booking2.setEnd(bookingDto2.getEnd());
        booking2.setItem(bookingDto2.getItem());
        booking2.setBooker(bookingDto2.getBooker());
        booking2.setStatus(bookingDto2.getStatus());
        bookingRepository.save(booking2);
        List<Item> items = new ArrayList<>();
        items.add(item);

        //do
        List<Booking> bookings = bookingRepository.findBookingsByItemInAndStatusOrderByStartDesc(items, StatusOfBooking.APPROVED);

        //check
        assertNotNull(booking.getId());
        assertThat(bookings.getLast().getId()).isEqualTo(booking2.getId());
        assertThat(bookings.getLast().getBooker().getId()).isEqualTo(booking2.getBooker().getId());
        assertThat(bookings.getLast().getItem().getId()).isEqualTo(item.getId());
    }
}
