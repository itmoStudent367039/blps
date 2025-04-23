package ru.ifmo.common.dto.hotels;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelDto {

    private String cityName;
    private String hotelName;
}
