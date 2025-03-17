package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestTest {


    @Test
    void shouldTestEquals() {
        //prepare
        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setId(1L);
        itemRequest1.setDescription("itemRequest1 description");

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setId(1L);
        itemRequest2.setDescription("itemRequest2 description");

        ItemRequest itemRequest3 = new ItemRequest();
        itemRequest3.setId(3L);
        itemRequest3.setDescription("itemRequest3 description");

        //check
        assertEquals(itemRequest1, itemRequest2);
        assertNotEquals(itemRequest1, itemRequest3);
    }

    @Test
    void shouldTestHashCode() {
        //prepare
        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setId(1L);
        itemRequest1.setDescription("itemRequest1 description");

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setId(1L);
        itemRequest2.setDescription("itemRequest2 description");

        //check
        assertEquals(itemRequest1.hashCode(), itemRequest2.hashCode());
    }
}