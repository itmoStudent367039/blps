package ru.ifmo.common.dto.hotels;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetHotelsRequest {

    @NotBlank
    private String cityName;

    @NotNull
    private LocalDate startBookingDate;

    @NotNull
    private LocalDate endBookingDate;

    @Min(value = 1)
    @Max(value = 10)
    private Integer guestsCount;
}
