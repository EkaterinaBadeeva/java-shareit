package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@ToString
public class Booking {
    //id — уникальный идентификатор бронирования
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //start — дата и время начала бронирования
    @Column(name = "start_date")
    LocalDateTime start;

    //end — дата и время конца бронирования
    @Column(name = "end_date")
    LocalDateTime end;

    //item — вещь, которую пользователь бронирует
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;

    //booker — пользователь, который осуществляет бронирование
    @ManyToOne
    @JoinColumn(name = "booker_id")
    User booker;

    //status — статус бронирования
    @Enumerated(EnumType.STRING)
    StatusOfBooking status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}