package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.StatusOfBooking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookingServiceImplTest {
    @Autowired
    BookingService bookingService;
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;

    BookingDtoShort booking1;
    BookingDtoShort booking2;
    UserDto userDto;
    User user;
    ItemWithRequestDto itemDto;
    Item item;

    @BeforeEach
    void beforeEach() {
        userDto = UserDto.builder().id(1L).name("test1").email("test1@test.ru").build();

        user = UserMapper.mapToUser(userService.create(userDto));
        itemDto = ItemWithRequestDto.builder()
                .name("item test")
                .description("item test description")
                .available(true)
                .build();
        item = ItemMapper.mapToItem(itemService.create(itemDto, user.getId()));
        booking1 = BookingDtoShort.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(10))
                .itemId(item.getId())
                .status(StatusOfBooking.WAITING)
                .build();
    }

    @Test
    void shouldCreateBooking() {
        //do
        BookingDto createdBooking = bookingService.create(booking1, user.getId());
        BookingDto newBooking = bookingService.getBookingById(createdBooking.getId(), user.getId());

        //check
        assertNotNull(newBooking.getId());
        assertThat(newBooking.getId()).isEqualTo(createdBooking.getId());
        assertThat(newBooking.getBooker().getName()).isEqualTo("test1");
        assertThat(newBooking.getBooker().getEmail()).isEqualTo("test1@test.ru");
        assertThat(newBooking.getItem().getName()).isEqualTo("item test");
    }

    @Test
    void shouldGetBookingById() {
        //prepare
        BookingDto createdBooking = bookingService.create(booking1, user.getId());

        //do
        BookingDto foundBooking = bookingService.getBookingById(createdBooking.getId(), user.getId());

        //check
        assertNotNull(foundBooking.getId());
        assertThat(foundBooking.getId()).isEqualTo(createdBooking.getId());
        assertThat(foundBooking.getBooker().getName()).isEqualTo("test1");
        assertThat(foundBooking.getBooker().getEmail()).isEqualTo("test1@test.ru");
        assertThat(foundBooking.getItem().getName()).isEqualTo("item test");
    }

    @Test
    void shouldApproved() {
        //prepare
        BookingDto createdBooking = bookingService.create(booking1, user.getId());

        //do
        BookingDto approvedBooking = bookingService.approved(true, createdBooking.getId(), user.getId());

        //check
        assertNotNull(approvedBooking.getId());
        assertThat(approvedBooking.getId()).isEqualTo(createdBooking.getId());
        assertThat(approvedBooking.getBooker().getName()).isEqualTo("test1");
        assertThat(approvedBooking.getBooker().getEmail()).isEqualTo("test1@test.ru");
        assertThat(approvedBooking.getItem().getName()).isEqualTo("item test");
        assertThat(approvedBooking.getStatus()).isEqualTo(StatusOfBooking.APPROVED);
    }

    @Test
    void shouldRejected() {
        //prepare
        BookingDto createdBooking = bookingService.create(booking1, user.getId());

        //do
        BookingDto approvedBooking = bookingService.approved(false, createdBooking.getId(), user.getId());

        //check
        assertNotNull(approvedBooking.getId());
        assertThat(approvedBooking.getId()).isEqualTo(createdBooking.getId());
        assertThat(approvedBooking.getBooker().getName()).isEqualTo("test1");
        assertThat(approvedBooking.getBooker().getEmail()).isEqualTo("test1@test.ru");
        assertThat(approvedBooking.getItem().getName()).isEqualTo("item test");
        assertThat(approvedBooking.getStatus()).isEqualTo(StatusOfBooking.REJECTED);
    }

    @Test
    void shouldFindBookingByBooker() {
        //prepare
        BookingDto createdBooking = bookingService.create(booking1, user.getId());

        //do
        Collection<BookingDto> foundBookings = bookingService.findBookingByBooker(State.ALL, user.getId());

        //check
        assertNotNull(foundBookings);
        assertThat(foundBookings).hasSize(1);
        assertTrue(foundBookings.contains(createdBooking));
    }

    @Test
    void shouldFindBookingByBookerWhenStateCurrent() {
        //prepare
        UserDto userDto2 = UserDto.builder().id(2L).name("test2").email("test2@test.ru").build();
        User user2 = UserMapper.mapToUser(userService.create(userDto2));
        BookingDto createdBooking = bookingService.create(booking1, user2.getId());

        //do
        Collection<BookingDto> foundBookings = bookingService.findBookingByBooker(State.CURRENT, user2.getId());

        //check
        assertNotNull(foundBookings);
        assertThat(foundBookings).hasSize(1);
        assertTrue(foundBookings.contains(createdBooking));
    }

    @Test
    void shouldFindBookingByBookerWhenStatePast() {
        //prepare
        UserDto userDto2 = UserDto.builder().id(2L).name("test2").email("test2@test.ru").build();
        User user2 = UserMapper.mapToUser(userService.create(userDto2));
        BookingDto createdBooking = bookingService.create(booking1, user2.getId());

        BookingDtoShort booking = BookingDtoShort.builder()
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().minusHours(1))
                .itemId(item.getId())
                .status(StatusOfBooking.APPROVED)
                .build();
        BookingDto createdBooking2 = bookingService.create(booking, user2.getId());

        //do
        Collection<BookingDto> foundBookings = bookingService.findBookingByBooker(State.PAST, user2.getId());

        //check
        assertNotNull(foundBookings);
        assertThat(foundBookings).hasSize(1);
        assertTrue(foundBookings.contains(createdBooking2));
    }

    @Test
    void shouldFindBookingByBookerWhenStateFuture() {
        //prepare
        UserDto userDto2 = UserDto.builder().id(2L).name("test2").email("test2@test.ru").build();
        User user2 = UserMapper.mapToUser(userService.create(userDto2));
        BookingDto createdBooking = bookingService.create(booking1, user2.getId());

        BookingDtoShort booking = BookingDtoShort.builder()
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().minusHours(1))
                .itemId(item.getId())
                .status(StatusOfBooking.APPROVED)
                .build();
        BookingDto createdBooking2 = bookingService.create(booking, user2.getId());

        //do
        Collection<BookingDto> foundBookings = bookingService.findBookingByBooker(State.FUTURE, user2.getId());

        //check
        assertNotNull(foundBookings);
        assertThat(foundBookings).hasSize(1);
        assertTrue(foundBookings.contains(createdBooking));
    }

    @Test
    void shouldFindBookingByBookerWhenStateWaiting() {
        //prepare
        UserDto userDto2 = UserDto.builder().id(2L).name("test2").email("test2@test.ru").build();
        User user2 = UserMapper.mapToUser(userService.create(userDto2));
        BookingDto createdBooking = bookingService.create(booking1, user2.getId());

        BookingDtoShort booking = BookingDtoShort.builder()
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().minusHours(1))
                .itemId(item.getId())
                .status(StatusOfBooking.WAITING)
                .build();
        BookingDto createdBooking2 = bookingService.create(booking, user2.getId());

        //do
        Collection<BookingDto> foundBookings = bookingService.findBookingByBooker(State.WAITING, user2.getId());

        //check
        assertNotNull(foundBookings);
        assertThat(foundBookings).hasSize(2);
        assertTrue(foundBookings.contains(createdBooking));
        assertTrue(foundBookings.contains(createdBooking2));
    }

    @Test
    void shouldFindBookingByBookerWhenStateRejected() {
        //prepare
        UserDto userDto2 = UserDto.builder().id(2L).name("test2").email("test2@test.ru").build();
        User user2 = UserMapper.mapToUser(userService.create(userDto2));
        BookingDto createdBooking = bookingService.create(booking1, user2.getId());

        BookingDtoShort booking = BookingDtoShort.builder()
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().minusHours(1))
                .itemId(item.getId())
                .status(StatusOfBooking.WAITING)
                .build();
        BookingDto createdBooking2 = bookingService.create(booking, user2.getId());
        bookingService.approved(false, createdBooking2.getId(), user.getId());

        //do
        Collection<BookingDto> foundBookings = bookingService.findBookingByBooker(State.REJECTED, user2.getId());

        //check
        assertNotNull(foundBookings);
        assertThat(foundBookings).hasSize(1);
        assertEquals(foundBookings.stream().findFirst().get().getStatus(), StatusOfBooking.REJECTED);
    }

    @Test
    void shouldFindBookingByOwner() {
        //prepare
        BookingDto createdBooking = bookingService.create(booking1, user.getId());

        //do
        Collection<BookingDto> foundBookings = bookingService.findBookingByOwner(State.ALL, user.getId());

        //check
        assertNotNull(foundBookings);
        assertThat(foundBookings).hasSize(1);
        assertTrue(foundBookings.contains(createdBooking));
    }

    @Test
    void shouldFindBookingByOwnerWhenStateCurrent() {
        //prepare
        BookingDto createdBooking = bookingService.create(booking1, user.getId());
        BookingDtoShort booking = BookingDtoShort.builder()
                .start(LocalDateTime.now().minusHours(1))
                .end(LocalDateTime.now().plusHours(10))
                .itemId(item.getId())
                .status(StatusOfBooking.APPROVED)
                .build();
        BookingDto createdBooking2 = bookingService.create(booking, user.getId());

        //do
        Collection<BookingDto> foundBookings = bookingService.findBookingByOwner(State.CURRENT, user.getId());

        //check
        assertNotNull(foundBookings);
        assertThat(foundBookings).hasSize(2);
        assertTrue(foundBookings.contains(createdBooking));
        assertTrue(foundBookings.contains(createdBooking2));
    }

    @Test
    void shouldFindBookingByOwnerWhenStatePast() {
        //prepare
        BookingDto createdBooking = bookingService.create(booking1, user.getId());
        BookingDtoShort booking = BookingDtoShort.builder()
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().minusHours(1))
                .itemId(item.getId())
                .status(StatusOfBooking.APPROVED)
                .build();
        BookingDto createdBooking2 = bookingService.create(booking, user.getId());

        //do
        Collection<BookingDto> foundBookings = bookingService.findBookingByOwner(State.PAST, user.getId());

        //check
        assertNotNull(foundBookings);
        assertThat(foundBookings).hasSize(1);
        assertTrue(foundBookings.contains(createdBooking2));
    }

    @Test
    void shouldFindBookingByOwnerWhenStateFuture() {
        //prepare
        BookingDto createdBooking = bookingService.create(booking1, user.getId());
        BookingDtoShort booking2 = BookingDtoShort.builder()
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().minusHours(1))
                .itemId(item.getId())
                .status(StatusOfBooking.APPROVED)
                .build();
        BookingDto createdBooking2 = bookingService.create(booking2, user.getId());

        //do
        Collection<BookingDto> foundBookings = bookingService.findBookingByOwner(State.FUTURE, user.getId());

        //check
        assertNotNull(foundBookings);
        assertThat(foundBookings).hasSize(1);
        assertTrue(foundBookings.contains(createdBooking));
    }

    @Test
    void shouldFindBookingByOwnerWhenStateWaiting() {
        //prepare
        bookingService.create(booking1, user.getId());
        BookingDtoShort booking = BookingDtoShort.builder()
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().minusHours(1))
                .itemId(item.getId())
                .status(StatusOfBooking.WAITING)
                .build();
        bookingService.create(booking, user.getId());

        //do
        Collection<BookingDto> foundBookings = bookingService.findBookingByOwner(State.WAITING, user.getId());

        //check
        assertNotNull(foundBookings);
        assertThat(foundBookings).hasSize(2);

    }

    @Test
    void shouldFindBookingByOwnerWhenStateRejected() {
        //prepare
        bookingService.create(booking1, user.getId());
        BookingDtoShort booking = BookingDtoShort.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(10))
                .itemId(item.getId())
                .status(StatusOfBooking.WAITING)
                .build();
        BookingDto createdBooking2 = bookingService.create(booking, user.getId());
        bookingService.approved(false, createdBooking2.getId(), user.getId());

        //do
        Collection<BookingDto> foundBookings = bookingService.findBookingByOwner(State.REJECTED, user.getId());

        //check
        assertNotNull(foundBookings);
        assertThat(foundBookings).hasSize(1);
        assertEquals(foundBookings.stream().findFirst().get().getStatus(), StatusOfBooking.REJECTED);
    }

    @Test
    void shouldThrowExceptionWhenItemIsNotAvailable() {
        //prepare
        LocalDateTime start = LocalDateTime.of(2025, 2, 2, 22, 22);
        LocalDateTime end = LocalDateTime.of(2026, 2, 2, 22, 22);
        ItemWithRequestDto itemDtoIsNotAvailable = ItemWithRequestDto.builder()
                .name("item test")
                .description("item test description")
                .available(false)
                .build();
        Item itemIsNotAvailable = ItemMapper.mapToItem(itemService.create(itemDtoIsNotAvailable, user.getId()));
        BookingDtoShort booking = BookingDtoShort.builder()
                .start(start)
                .end(end)
                .itemId(itemIsNotAvailable.getId())
                .status(StatusOfBooking.WAITING)
                .build();

        //check
        assertThatThrownBy(() -> bookingService.create(booking, user.getId()))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void shouldThrowExceptionWhenBookingNotFind() {
        //prepare
        bookingService.create(booking1, user.getId());

        //check
        assertThatThrownBy(() -> bookingService.getBookingById(1000L, user.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenGetBookingNotBookerOrOwner() {
        //prepare
        UserDto userDto2 = UserDto.builder().id(2L).name("test2").email("test2@test.ru").build();
        User user2 = UserMapper.mapToUser(userService.create(userDto2));
        BookingDto bookingTest = bookingService.create(booking1, user.getId());

        //check
        assertThatThrownBy(() -> bookingService.getBookingById(bookingTest.getId(), user2.getId()))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void shouldThrowExceptionWhenIdNotFind() {
        //prepare
        bookingService.create(booking1, user.getId());

        //check
        assertThatThrownBy(() -> bookingService.getBookingById(null, user.getId()))
                .isInstanceOf(ValidationException.class);
    }
}