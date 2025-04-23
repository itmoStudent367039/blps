package ru.ifmo.monolith.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.monolith.favorites.Favorite;
import ru.ifmo.monolith.favorites.FavoritesRepository;

@Service
@RequiredArgsConstructor
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;

    @Transactional
    public void save(String username, String hotelName) {
        var favorite = new Favorite();
        favorite.setUsername(username);
        favorite.setHotelName(hotelName);
        favoritesRepository.save(favorite);
    }
}
