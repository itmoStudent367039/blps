package ru.ifmo.monolith.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelDto {

    private String cityName;
    private String hotelName;
}
