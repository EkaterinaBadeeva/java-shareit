package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.Objects;

/**
 * TODO Sprint add-item-requests.
 */

@Entity
@Table(name = "requests")
@Getter
@Setter
@ToString
public class ItemRequest {
    //id — уникальный идентификатор запроса
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //description — текст запроса, содержащий описание требуемой вещи
    @Column(name = "description", nullable = false)
    String description;

    //requestor — пользователь, создавший запрос
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    User requestor;

    //created — дата и время создания запроса
    Instant created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequest that = (ItemRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
