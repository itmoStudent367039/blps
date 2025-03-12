package ru.ifmo.monolith.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class FavoriteCityResponse {

    private List<CityDto> favoriteCities;
}
