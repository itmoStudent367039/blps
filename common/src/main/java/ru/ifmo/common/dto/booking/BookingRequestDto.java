package ru.ifmo.common.dto.booking;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingRequestDto {

    @NotNull
    private BookingInfoDto bookingInfo;

    @NotBlank
    private String email;

    @NotBlank
    private String lastName;

    @NotBlank
    private String firstName;

    @Data
    @Builder
    public static class BookingInfoDto {

        @NotBlank
        private String hotelName;

        @NotBlank
        private String hotelNumberName;

        @NotBlank
        private String tariffName;

        private LocalDate startBookingDate;

        private LocalDate endBookingDate;

        @Min(1)
        @Max(value = 10)
        private Integer guestsNumber;
    }
}
