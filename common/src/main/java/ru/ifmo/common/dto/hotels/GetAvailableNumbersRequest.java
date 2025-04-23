package ru.ifmo.common.dto.hotels;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetAvailableNumbersRequest {

    @NotBlank
    private String hotelName;

    @Min(1)
    @Max(5)
    private int guestsCount;

    private LocalDate startBookingDate;
    private LocalDate endBookingDate;
}
