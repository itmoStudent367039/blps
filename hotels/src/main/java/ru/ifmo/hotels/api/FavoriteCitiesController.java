package ru.ifmo.hotels.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.common.dto.hotels.FavoriteCityResponse;
import ru.ifmo.hotels.service.CitiesService;

import static org.springframework.http.ResponseEntity.ok;
import static ru.ifmo.hotels.api.constant.ApiUri.FAVORITES_CITIES_URI;

@RequiredArgsConstructor
@RestController
@RequestMapping(FAVORITES_CITIES_URI)
public class FavoriteCitiesController {

    private final CitiesService citiesService;

    @GetMapping
    public ResponseEntity<FavoriteCityResponse> getFavorites() {
        return ok(FavoriteCityResponse.builder()
                .favoriteCities(citiesService.getFavorites())
                .build());
    }
}
