package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusOfBooking;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Transactional
public class ItemRequestRepositoryTest {

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
    ItemRequest itemRequest2 = new ItemRequest();
    ItemRequest itemRequest3 = new ItemRequest();

    @BeforeEach
    void beforeEach() {
        user1.setName("User1 name");
        user1.setEmail("user1@email.ru");
        user1 = userRepository.save(user1);

        user2.setName("User2 name");
        user2.setEmail("user2@email.ru");
        user2 = userRepository.save(user2);

        itemRequest.setId(1L);
        itemRequest.setDescription("itemRequest description");
        itemRequest.setRequestor(user2);
        itemRequest.setCreated(Instant.parse("2025-01-01T00:00:00Z"));
        itemRequest = itemRequestRepository.save(itemRequest);

        itemRequest2.setId(2L);
        itemRequest2.setDescription("itemRequest2 description");
        itemRequest2.setRequestor(user2);
        itemRequest2.setCreated(Instant.parse("2025-02-02T00:00:00Z"));
        itemRequest2 = itemRequestRepository.save(itemRequest2);

        itemRequest3.setId(3L);
        itemRequest3.setDescription("itemRequest2 description");
        itemRequest3.setRequestor(user1);
        itemRequest3.setCreated(Instant.parse("2025-01-01T00:00:00Z"));
        itemRequest3 = itemRequestRepository.save(itemRequest3);

        item.setName("Item1 name");
        item.setDescription("Item description");
        item.setIsAvailable(true);
        item.setOwner(user1);
        item.setRequest(itemRequest);
        item = itemRepository.save(item);

        booking.setId(1L);
        booking.setStart(LocalDateTime.of(2024, 1, 1, 11, 11));
        booking.setEnd(LocalDateTime.of(2024, 2, 2, 22, 22));
        booking.setItem(item);
        booking.setBooker(user2);
        booking.setStatus(StatusOfBooking.APPROVED);
        bookingRepository.save(booking);

        comment.setId(1L);
        comment.setText("comment");
        comment.setItem(item);
        comment.setAuthor(user2);
        comment.setCreated(Instant.parse("2025-01-01T00:00:00Z"));
        commentRepository.save(comment);
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
    void shouldFindAllItemRequestByRequestorIdOrderByCreatedDesc() {
        //do
        List<ItemRequest> itemRequests = itemRequestRepository
                .findAllItemRequestByRequestorIdOrderByCreatedDesc(user2.getId());

        //check
        assertFalse(itemRequests.isEmpty());
        assertNotNull(itemRequests.getFirst().getId());
        assertThat(itemRequests).hasSize(2);
        assertThat(itemRequests.getFirst().getId()).isEqualTo(itemRequest2.getId());
        assertThat(itemRequests.getFirst().getDescription()).isEqualTo(itemRequest2.getDescription());
    }

    @Test
    void shouldFindAllItemRequestByRequestorIdNotOrderByCreatedDesc() {
        //do
        List<ItemRequest> itemRequests = itemRequestRepository
                .findAllItemRequestByRequestorIdNotOrderByCreatedDesc(user2.getId());

        //check
        assertFalse(itemRequests.isEmpty());
        assertNotNull(itemRequests.getFirst().getId());
        assertThat(itemRequests).hasSize(1);
        assertThat(itemRequests.getFirst().getId()).isEqualTo(itemRequest3.getId());
        assertThat(itemRequests.getFirst().getDescription()).isEqualTo(itemRequest3.getDescription());
    }
}
