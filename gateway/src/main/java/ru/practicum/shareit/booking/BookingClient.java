package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.client.BaseClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    //POST /bookings
    // добавление нового запроса на бронирование
    public ResponseEntity<Object> create(BookingDtoShort bookingDto, Long userId) {
        return post("", userId, bookingDto);
    }

    //PATCH /bookings/{bookingId}?approved={approved}
    //подтверждение или отклонение запроса на бронирование
    public ResponseEntity<Object> approved(Boolean approved, Long bookingId, Long userId) {
        String url = "/" + bookingId + "?approved=" + approved;
        return patch(url, userId, null);
    }

    //GET /bookings/{bookingId}
    //получение запроса на бронирование по Id
    public ResponseEntity<Object> getBookingById(Long bookingId, Long userId) {
        return get("/" + bookingId, userId);
    }

    //GET /bookings?state={state}
    //получение списка всех бронирований текущего пользователя
    public ResponseEntity<Object> findBookingByBooker(State state, Long userId) {
        return get("?state=" + state, userId);
    }

    //GET /bookings/owner?state={state}
    //получение списка всех бронирований текущего пользователя(владельца вещи)
    public ResponseEntity<Object> findBookingByOwner(@Valid @RequestParam(defaultValue = "ALL") State state,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return get("/owner?state=" + state, userId);
    }
}