package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.dto.State;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    //POST /bookings
    // добавление нового запроса на бронирование
    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody BookingDtoShort bookingDto,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Добавление нового запроса на бронирование: booking {}, userId={}", bookingDto, userId);
        return bookingClient.create(bookingDto, userId);
    }

    //PATCH /bookings/{bookingId}?approved={approved}
    //подтверждение или отклонение запроса на бронирование
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approved(@Valid @RequestParam Boolean approved,
                                           @PathVariable Long bookingId,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Подтверждение или отклонение запроса на бронирование: approved {}, bookingId={}, userId={}",
                approved, bookingId, userId);
        return bookingClient.approved(approved, bookingId, userId);
    }

    //GET /bookings/{bookingId}
    //получение запроса на бронирование по Id
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable Long bookingId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение запроса на бронирование по Id: bookingId={}, userId={}", bookingId, userId);
        return bookingClient.getBookingById(bookingId, userId);
    }

    //GET /bookings?state={state}
    //получение списка всех бронирований текущего пользователя
    @GetMapping
    public ResponseEntity<Object> findBookingByBooker(@Valid @RequestParam(defaultValue = "ALL") State state,
                                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение списка всех бронирований текущего пользователя: state={}, userId={}", state, userId);
        return bookingClient.findBookingByBooker(state, userId);
    }

    //GET /bookings/owner?state={state}
    //получение списка всех бронирований текущего пользователя(владельца вещи)
    @GetMapping("/owner")
    public ResponseEntity<Object> findBookingByOwner(@Valid @RequestParam(defaultValue = "ALL") State state,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение списка всех бронирований текущего пользователя: state={}, userId={}", state, userId);
        return bookingClient.findBookingByOwner(state, userId);
    }
}
