package ru.ifmo.monolith.api.validator;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BookingDateValidator {

    public boolean isValid(LocalDate startBookingDate, LocalDate endBookingDate) {
        boolean isEndDateAfter = endBookingDate.isAfter(startBookingDate);
        boolean isStartDateValid = startBookingDate.isAfter(LocalDate.now()) ||
                startBookingDate.isEqual(LocalDate.now());
        return isEndDateAfter && isStartDateValid;
    }
}
