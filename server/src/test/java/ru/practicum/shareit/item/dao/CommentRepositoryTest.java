package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusOfBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;

    User user1 = new User();
    User user2 = new User();
    Item item = new Item();
    Booking booking = new Booking();
    Comment comment = new Comment();
    ItemRequest itemRequest = new ItemRequest();

    @BeforeEach
    void beforeEach() {

        user1.setName("User1 name");
        user1.setEmail("user1@email.ru");
        user1 = userRepository.save(user1);

        user2.setName("User2 name");
        user2.setEmail("user2@email.ru");
        user2 = userRepository.save(user2);

        item.setName("Item1 name");
        item.setDescription("Item description");
        item.setIsAvailable(true);
        item.setOwner(user1);
        item = itemRepository.save(item);

        booking.setId(1L);
        booking.setStart(LocalDateTime.of(2024, 1, 1, 11,11));
        booking.setEnd(LocalDateTime.of(2024, 2, 2, 22,22));
        booking.setItem(item);
        booking.setBooker(user2);
        booking.setStatus(StatusOfBooking.APPROVED);
        bookingRepository.save(booking);

        comment.setId(1L);
        comment.setText("comment");
        comment.setItem(item);
        comment.setAuthor(user2);
        comment.setCreated(Instant.parse("2025-01-01T00:00:00Z"));
        comment = commentRepository.save(comment);

        itemRequest.setId(1L);
        itemRequest.setDescription("itemRequest description");
        itemRequest.setRequestor(user2);
        itemRequest.setCreated(Instant.parse("2025-01-01T00:00:00Z"));
        itemRequestRepository.save(itemRequest);
    }

    @AfterEach
    void afterEach() {
        itemRequestRepository.deleteAll();
        commentRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldFindCommentsByItemId() {

        //do
        List<Comment> comments = commentRepository.findCommentsByItemId(item.getId());

        //check
        assertNotNull(comments.getLast().getId());
        assertThat(comments.getLast().getId()).isEqualTo(comment.getId());
        assertThat(comments.getLast().getItem().getId()).isEqualTo(item.getId());
    }

    @Test
    void shouldFindCommentsByItemIn() {
        //prepare
        List<Item> items = new ArrayList<>();
        items.add(item);

        //do
        List<Comment> comments = commentRepository.findCommentsByItemIn(items);

        //check
        assertNotNull(comments.getLast().getId());
        assertThat(comments.getLast().getId()).isEqualTo(comment.getId());
        assertThat(comments.getLast().getItem().getId()).isEqualTo(item.getId());
    }
}