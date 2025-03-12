package ru.ifmo.monolith.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DestinationsResponse {

    private List<CityDto> cities;
    private List<HotelDto> hotels;
}
