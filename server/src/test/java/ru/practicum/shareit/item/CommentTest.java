package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void shouldTestEquals() {
        //prepare
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("comment1");
        comment1.setCreated(Instant.now());

        Comment comment2 = new Comment();
        comment2.setId(1L);
        comment2.setText("comment2");
        comment2.setCreated(Instant.now());

        Comment comment3 = new Comment();
        comment3.setId(3L);
        comment3.setText("comment3");
        comment3.setCreated(Instant.now());

        //check
        assertEquals(comment1, comment2);
        assertNotEquals(comment1, comment3);
    }

    @Test
    void shouldTestHashCode() {
        //prepare
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("comment1");
        comment1.setCreated(Instant.now());

        Comment comment2 = new Comment();
        comment2.setId(1L);
        comment2.setText("comment2");
        comment2.setCreated(Instant.now());

        //check
        assertEquals(comment1.hashCode(), comment2.hashCode());
    }
}