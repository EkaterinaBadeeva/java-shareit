package ru.practicum.shareit.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingDtoShort;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, BookingDtoShort> {
    @Override
    public boolean isValid(BookingDtoShort bookingDtoShort, ConstraintValidatorContext context) {
        return bookingDtoShort.getStart().isBefore(bookingDtoShort.getEnd());
    }
}