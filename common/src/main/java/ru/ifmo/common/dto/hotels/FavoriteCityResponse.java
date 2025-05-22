package ru.ifmo.common.dto.hotels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
public class FavoriteCityResponse {

    private List<CityDto> favoriteCities;
}
