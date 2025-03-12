package ru.ifmo.monolith.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.monolith.service.CitiesService;
import ru.ifmo.monolith.dto.FavoriteCityResponse;

import static org.springframework.http.ResponseEntity.ok;
import static ru.ifmo.monolith.api.constant.ApiUri.FAVORITES_URI;

@RequiredArgsConstructor
@RestController
@RequestMapping(FAVORITES_URI)
public class FavoriteCitiesController {

    private final CitiesService citiesService;

    @GetMapping
    public ResponseEntity<FavoriteCityResponse> getFavorites() {
        return ok(FavoriteCityResponse.builder()
                .favoriteCities(citiesService.getFavorites())
                .build());
    }
}
