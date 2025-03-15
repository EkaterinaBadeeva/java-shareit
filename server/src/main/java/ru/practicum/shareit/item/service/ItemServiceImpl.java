package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusOfBooking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;


    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public Collection<ItemDto> getAllItemsOfUser(Long userId) {
        log.info("Получение списка всех вещей.");
        checkId(userId);
        checkUser(userId);

        List<ItemDto> itemsOfUser = new ArrayList<>();
        List<Item> items = itemRepository.findAllItemsByOwnerId(userId);

        List<Comment> comments = commentRepository.findCommentsByItemIn(items);
        Map<Long, List<CommentDto>> commentOfItem = new HashMap<>();

        List<Booking> bookings = bookingRepository.findBookingsByItemInAndStatusOrderByStartDesc(items, StatusOfBooking.APPROVED);
        Map<Long, List<BookingDtoShort>> bookingsOfItem = new HashMap<>();

        List<CommentDto> commentsDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentsDto.add(CommentMapper.mapToCommentDto(comment));
            commentOfItem.put(comment.getItem().getId(), commentsDto);
        }

        List<BookingDtoShort> bookingDto = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingDto.add(BookingMapper.mapToBookingDtoShort(booking));
            bookingsOfItem.put(booking.getItem().getId(), bookingDto);
        }

        LocalDateTime now = LocalDateTime.now();
        for (Item item : items) {
            BookingDtoShort nextBooking = null;
            BookingDtoShort lastBooking = null;
            List<BookingDtoShort> bookingsOfItemDto = bookingsOfItem.get(item.getId());

            if (bookingsOfItemDto != null) {
                for (BookingDtoShort booking : bookingsOfItemDto) {
                    if (booking.getStart().isAfter(now)) {
                        nextBooking = booking;
                    }
                    if (booking.getEnd().isBefore(now)) {
                        lastBooking = booking;
                        break;
                    }
                }
            }
            List<BookingDtoShort> listOfBookings = new ArrayList<>(List.of());
            listOfBookings.add(nextBooking);
            listOfBookings.add(lastBooking);

            Long itemId = item.getId();
            ItemDto itemDto = ItemMapper.mapToItemWithCommentsAndBookingDto(item, commentOfItem.get(itemId), lastBooking, nextBooking);
            itemsOfUser.add(itemDto);
        }

        return itemsOfUser;
    }

    @Override
    public ItemDto getItemById(Long id, Long userId) {
        log.info("Получение вещи по Id.");
        checkId(id);
        checkId(userId);
        checkUser(userId);
        List<CommentDto> comments;

        BookingDtoShort nextBooking;
        BookingDtoShort lastBooking;

        comments = commentRepository.findCommentsByItemId(id).stream()
                .map(CommentMapper::mapToCommentDto)
                .toList();

        Optional<Item> itemOpt = itemRepository.findById(id);

        if (itemOpt.isPresent() && itemOpt.get().getOwner().getId().equals(userId)) {
            Optional<Booking> next = bookingRepository.findFirstBookingByItemIdAndStartIsAfterOrderByStart(id, LocalDateTime.now());
            Optional<Booking> last = bookingRepository.findFirstBookingByItemIdAndEndIsBeforeOrderByEndDesc(id, LocalDateTime.now());

            if (next.isPresent() || last.isPresent()) {
                if (next.isPresent()) {
                    nextBooking = BookingMapper.mapToBookingDtoShort(next.orElse(new Booking()));
                } else {
                    nextBooking = null;
                }
                if (last.isPresent()) {
                    lastBooking = BookingMapper.mapToBookingDtoShort(last.orElse(new Booking()));
                } else {
                    lastBooking = null;
                }
                return itemOpt.map(item -> ItemMapper.mapToItemWithCommentsAndBookingDto(item, comments, lastBooking, nextBooking))
                        .orElseThrow(() -> new NotFoundException("Вещь с id " + id + " не найдена"));
            }
        }

        return itemRepository.findById(id)
                .map(item -> ItemMapper.mapToItemWithCommentsDto(item, comments))
                .orElseThrow(() -> new NotFoundException("Вещь с id " + id + " не найдена"));
    }

    @Override
    @Transactional
    public ItemDto create(ItemWithRequestDto itemDto, Long userId) {
        log.info("Добавление новой вещи.");
        checkId(userId);
        checkUser(userId);
        Long requestId = itemDto.getRequestId();

        ItemRequest request = null;
        if (requestId != null) {
            request = itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос не найден"));
        }

        Item item = ItemMapper.mapToItemWithRequest(itemDto, request);

        item.setOwner(UserMapper.mapToUser(userService.findUserById(userId)));
        item = itemRepository.save(item);
        checkId(item.getId());
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    @Transactional
    public ItemDto update(Map<String, String> fields, Long itemId, Long userId) {
        log.info("Редактирование вещи.");
        checkId(itemId);
        checkId(userId);
        checkUser(userId);

        Item oldItem = ItemMapper.mapToItem(getItemById(itemId, userId));

        if (!oldItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Вещь " + oldItem.getName() + " в списке пользователя" +
                    " не найдена");
        }
        String newName = fields.get("name");
        String newDescription = fields.get("description");
        String newAvailable = fields.get("available");

        if (newName != null) {
            oldItem.setName(newName);
        }

        if (newDescription != null) {
            oldItem.setDescription(newDescription);
        }

        if (newAvailable != null) {
            oldItem.setIsAvailable(Boolean.valueOf(newAvailable));
        }

        return ItemMapper.mapToItemDto(oldItem);
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        log.info("Поиск вещи потенциальным арендатором");

        if (text.isEmpty()) {
            return List.of();
        }

        Collection<Item> findingItems = itemRepository.search(text);

        return findingItems.stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto createComment(CommentDto commentDto, Long itemId, Long userId) {
        log.info("Добавление коментария к вещи.");
        checkId(itemId);
        checkId(userId);
        checkUser(userId);

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена"));

        bookingRepository
                .findFirstBookingsByBookerIdAndItemIdAndEndIsBeforeOrderByEndDesc(userId, itemId, LocalDateTime.now())
                .orElseThrow(() -> new ValidationException("Пользователь " + userId + " не бронировал вещь " + itemId));

        Comment comment = CommentMapper.mapToComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(UserMapper.mapToUser(userService.findUserById(userId)));
        comment.setCreated(Instant.now());
        comment = commentRepository.save(comment);
        checkId(comment.getId());

        return CommentMapper.mapToCommentDto(comment);
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
}
