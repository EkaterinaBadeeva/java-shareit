package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void shouldTestEquals() {
        //prepare
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item1 name");
        item1.setDescription("Item description");
        item1.setIsAvailable(true);

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("Item2 name");
        item2.setDescription("Item2 description");
        item2.setIsAvailable(true);

        Item item3 = new Item();
        item3.setId(3L);
        item3.setName("Item3 name");
        item3.setDescription("Item3 description");
        item3.setIsAvailable(true);

        //check
        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
    }

    @Test
    void shouldTestHashCode() {
        //prepare
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item1 name");
        item1.setDescription("Item description");
        item1.setIsAvailable(true);

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("Item2 name");
        item2.setDescription("Item2 description");
        item2.setIsAvailable(true);

        //check
        assertEquals(item1.hashCode(), item2.hashCode());
    }
}