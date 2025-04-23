package ru.ifmo.common.validator;

import java.time.LocalDate;

public class BookingDateValidator {

    public boolean isValid(LocalDate startBookingDate, LocalDate endBookingDate) {
        boolean isEndDateAfter = endBookingDate.isAfter(startBookingDate);
        boolean isStartDateValid = startBookingDate.isAfter(LocalDate.now()) ||
                startBookingDate.isEqual(LocalDate.now());
        return isEndDateAfter && isStartDateValid;
    }
}
