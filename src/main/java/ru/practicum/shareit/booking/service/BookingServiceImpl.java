package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.StatusOfBooking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional
    public BookingDto create(BookingDtoShort bookingDto, Long userId) {
        log.info("Добавление нового запроса на бронирование.");
        checkId(userId);
        checkUser(userId);

        Booking booking = BookingMapper.mapToBookingShort(bookingDto);
        Item item = ItemMapper.mapToItem(itemService.getItemById(bookingDto.getItemId(), userId));

        booking.setBooker(UserMapper.mapToUser(userService.findUserById(userId)));
        booking.setItem(item);
        booking.setStatus(StatusOfBooking.WAITING);
        checkDateOfBookingAndAvailableOfItem(booking);

        booking = bookingRepository.save(booking);
        checkId(booking.getId());
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long id, Long userId) {
        log.info("Получение запроса на бронирование по Id.");
        checkId(id);
        checkId(userId);
        checkUser(userId);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + id + " не найдено"));

        if (!(booking.getBooker().getId().equals(userId) ||
                booking.getItem().getOwner().getId().equals(userId))) {
            throw new ValidationException("Пользователь с id = "
                    + userId + " не является автором бронирования номер "
                    + id + ", либо владельцем вещи " + booking.getItem().getName());
        }

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto approved(Boolean approved, Long bookingId, Long userId) {
        log.info("Подтверждение или отклонение запроса на бронирование.");
        checkId(bookingId);
        checkId(userId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            log.warn("Пользователь не является владельцем вещи.");
            throw new ValidationException("Пользователь с Id = " + userId + " не является владельцем вещи " + booking.getItem().getName());
        }

        if (approved == Boolean.TRUE) {
            booking.setStatus(StatusOfBooking.APPROVED);
        }

        if (approved == Boolean.FALSE) {
            booking.setStatus(StatusOfBooking.REJECTED);
        }

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> findBookingByBooker(State state, Long userId) {
        log.info("Получение списка всех бронирований текущего пользователя.");
        checkId(userId);
        checkUser(userId);
        Collection<Booking> bookings = List.of();
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllBookingsByBookerIdOrderByEndDesc(userId);
                break;

            case CURRENT:
                bookings = bookingRepository.findAllBookingsByBookerIdAndEndIsAfterOrderByEndDesc(userId, now);
                break;

            case PAST:
                bookings = bookingRepository.findAllBookingsByBookerIdAndEndIsBeforeOrderByEndDesc(userId, now);

                break;

            case FUTURE:
                bookings = bookingRepository.findAllBookingsByBookerIdAndStartIsAfterOrderByEndDesc(userId, now);
                break;

            case WAITING:
                bookings =
                        bookingRepository.findAllBookingsByBookerIdAndStatusOrderByEndDesc(userId, StatusOfBooking.WAITING);
                break;

            case REJECTED:
                bookings =
                        bookingRepository.findAllBookingsByBookerIdAndStatusOrderByEndDesc(userId, StatusOfBooking.REJECTED);
                break;
        }

        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BookingDto> findBookingByOwner(State state, Long userId) {
        log.info("Получение списка всех бронирований текущего пользователя (владельца вещи).");
        checkId(userId);
        checkUser(userId);
        Collection<Booking> bookings = List.of();
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllBookingsByItemOwnerIdOrderByEndDesc(userId);
                break;

            case CURRENT:
                bookings = bookingRepository.findAllBookingsByItemOwnerIdAndEndIsAfterOrderByEndDesc(userId, now);
                break;

            case PAST:
                bookings = bookingRepository.findAllBookingsByItemOwnerIdAndEndIsBeforeOrderByEndDesc(userId, now);

                break;

            case FUTURE:
                bookings = bookingRepository.findAllBookingsByItemOwnerIdAndStartIsAfterOrderByEndDesc(userId, now);
                break;

            case WAITING:
                bookings =
                        bookingRepository.findAllBookingsByItemOwnerIdAndStatusOrderByEndDesc(userId, StatusOfBooking.WAITING);
                break;

            case REJECTED:
                bookings =
                        bookingRepository.findAllBookingsByItemOwnerIdAndStatusOrderByEndDesc(userId, StatusOfBooking.REJECTED);
                break;
        }

        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
    }

    private void checkId(Long id) {
        if (id == null) {
            log.warn("Id должен быть указан.");
            throw new ValidationException("Id должен быть указан");
        }
    }

    private void checkUser(Long userId) {
        if (userService.findUserById(userId) == null) {
            log.warn("Неверно указан Id пользователя.");
            throw new NotFoundException("Пользователь с Id = " + userId + " не найден");

        }
    }

    private void checkDateOfBookingAndAvailableOfItem(Booking booking) {
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();

        if (start.isAfter(end)) {
            throw new ValidationException("Дата и время начала бронирования позже даты и времени конца бронирования");
        } else if (start.equals(end)) {
            throw new ValidationException("Дата и время начала бронирования равна дате и времени конца бронирования");
        }

        if (!booking.getItem().getIsAvailable()) {
            throw new ValidationException("Вещь не доступна для аренды");
        }
    }
}
