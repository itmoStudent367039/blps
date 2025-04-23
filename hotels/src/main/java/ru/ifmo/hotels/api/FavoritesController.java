package ru.ifmo.hotels.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.common.dto.hotels.GetFavoriteHotelsResponse;
import ru.ifmo.hotels.service.FavoritesHotelsService;

import static ru.ifmo.hotels.api.constant.ApiUri.FAVORITES_URI;

@RestController
@RequestMapping(FAVORITES_URI)
@RequiredArgsConstructor
public class FavoritesController {

    private final FavoritesHotelsService favoritesHotelsService;

    @GetMapping
    public ResponseEntity<GetFavoriteHotelsResponse> getFavorites(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(favoritesHotelsService.getAll(user.getUsername()));
    }
}
