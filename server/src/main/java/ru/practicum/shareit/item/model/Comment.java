package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
public class Comment {
    //id — уникальный идентификатор комментария о вещи
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //text — содержимое комментария
    String text;

    //item — вещь, к которой относится комментарий
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;

    //author — автор комментария
    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;

    //created — дата создания комментария
    Instant created = Instant.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}