package ru.practicum.shareit.booking.dto;

public enum State {
    ALL,  // все бронирования
    CURRENT, // текущие бронирования
    PAST, // завершённые бронирования
    FUTURE,  // будущие бронирования
    WAITING,  // ожидающие подтверждения бронирования
    REJECTED  // отклонённые бронирования
}