package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {


    @Test
    void shouldTestEquals() {
        //prepare
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User1 name");
        user1.setEmail("user1@email.ru");

        User user2 = new User();
        user2.setId(1L);
        user2.setName("User2 name");
        user2.setEmail("user1@email.ru");

        User user3 = new User();
        user3.setId(1L);
        user3.setName("User3 name");
        user3.setEmail("user3@email.ru");

        User user4 = new User();
        user4.setId(4L);
        user4.setName("User4 name");
        user4.setEmail("user4@email.ru");

        //check
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, user4);
    }

    @Test
    void shouldTestHashCode() {
        //prepare
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User1 name");
        user1.setEmail("user1@email.ru");

        User user2 = new User();
        user2.setId(1L);
        user2.setName("User2 name");
        user2.setEmail("user1@email.ru");

        //check
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}