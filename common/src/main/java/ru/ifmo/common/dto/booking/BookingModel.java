package ru.ifmo.common.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingModel {

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer guestsNumber;

    private String firstName;

    private String lastName;

    private String email;

    private String hotelNumberName;

    private String tariffName;

    private String hotelName;

    private boolean isPayed;
}
