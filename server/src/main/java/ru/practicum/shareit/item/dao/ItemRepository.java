package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllItemsByOwnerId(long userId);

    List<Item> findAllItemsByRequestIdIn(List<Long> itemRequestIds);

    List<Item> findAllItemsByRequestId(Long itemRequestId);

    @Query(" select i from Item i " +
            "where i.isAvailable = true and (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> search(String text);
}