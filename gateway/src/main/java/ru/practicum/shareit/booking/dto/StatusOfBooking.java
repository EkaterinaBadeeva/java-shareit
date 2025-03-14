package ru.practicum.shareit.booking.dto;

public enum StatusOfBooking {
    WAITING,  // новое бронирование, ожидает одобрения
    APPROVED, // бронирование подтверждено владельцем
    REJECTED, // бронирование отклонено владельцем
    CANCELED  // бронирование отменено создателем
}
