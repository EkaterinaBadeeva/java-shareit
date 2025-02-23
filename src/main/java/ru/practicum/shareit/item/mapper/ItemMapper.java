package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();
    }

    public static Item mapToItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());
        item.setOwner(itemDto.getOwner());
        item.setRequest(itemDto.getRequest());

        return item;
    }

    public static ItemWithBookingDto mapToItemWithBookingDto(Item item, List <BookingDtoShort> bookings, List<CommentDto> comments) {
        return ItemWithBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .owner(item.getOwner())
                .bookings(bookings)
                .request(item.getRequest())
                .comments(comments)
                .build();
    }
    public static ItemDto mapToItemWithCommentsDto(Item item, List<CommentDto> comments
                                                   ) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .comments(comments)
                .build();
    }
    public static ItemDto mapToItemWithCommentsAndBookingDto(Item item, List<CommentDto> comments,
                                                   BookingDtoShort lastBooking, BookingDtoShort nextBooking) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .comments(comments)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();
    }
}