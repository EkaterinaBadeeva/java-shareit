package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Objects;

@Entity
@Table(name = "items")
@Getter
@Setter
@ToString
public class Item {
    //id — уникальный идентификатор вещи
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //name — краткое название
    @Column(name = "name", nullable = false)
    String name;

    //description — развёрнутое описание
    @Column(name = "description", nullable = false)
    String description;

    //is_available — статус о том, доступна или нет вещь для аренды
    @Column(name = "is_available")
    Boolean isAvailable;

    //owner — владелец вещи
    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;

    //request — если вещь была создана по запросу другого пользователя, то в этом
    //поле будет храниться ссылка на соответствующий запрос
    @ManyToOne
    @JoinColumn(name = "request_id")
    ItemRequest request;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}