package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusOfBooking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllBookingsByBookerIdOrderByEndDesc(long userId);

    List<Booking> findAllBookingsByBookerIdAndEndIsAfterOrderByEndDesc(long userId, LocalDateTime now);

    List<Booking> findAllBookingsByBookerIdAndEndIsBeforeOrderByEndDesc(long userId, LocalDateTime now);

    List<Booking> findAllBookingsByBookerIdAndStartIsAfterOrderByEndDesc(long userId, LocalDateTime now);

    List<Booking> findAllBookingsByBookerIdAndStatusOrderByEndDesc(long userId, StatusOfBooking status);

    List<Booking> findAllBookingsByItemOwnerIdOrderByEndDesc(long userId);

    List<Booking> findAllBookingsByItemOwnerIdAndEndIsAfterOrderByEndDesc(long userId, LocalDateTime now);

    List<Booking> findAllBookingsByItemOwnerIdAndEndIsBeforeOrderByEndDesc(long userId, LocalDateTime now);

    List<Booking> findAllBookingsByItemOwnerIdAndStartIsAfterOrderByEndDesc(long userId, LocalDateTime now);

    List<Booking> findAllBookingsByItemOwnerIdAndStatusOrderByEndDesc(long userId, StatusOfBooking status);

    List<Booking> findBookingsByItemId(long itemId);

    Optional<Booking> findFirstBookingByItemIdAndEndIsBeforeOrderByEndDesc(long itemId, LocalDateTime now);

    Optional<Booking> findFirstBookingByItemIdAndStartIsAfterOrderByStart(long itemId, LocalDateTime now);

    Optional<Booking> findFirstBookingsByBookerIdAndItemIdAndEndIsBeforeOrderByEndDesc(long userId, long itemId, LocalDateTime now);
}