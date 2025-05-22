package ru.ifmo.common.dto.hotels;

import lombok.Data;

import java.util.List;

@Data
public class GetFavoriteHotelsResponse {

    private List<HotelDto> favoriteHotels;
}
