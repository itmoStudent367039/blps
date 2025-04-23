package ru.ifmo.hotels.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.common.dto.hotels.GetFavoriteHotelsResponse;
import ru.ifmo.hotels.domain.repository.FavoritesRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoritesHotelsService {

    private final FavoritesRepository favoritesRepository;
    private final HotelsService hotelsService;

    public GetFavoriteHotelsResponse getAll(String user) {
        var favoriteCities = favoritesRepository.findAllByUsername(user)
                .stream()
                .map(favorite -> hotelsService.findByHotelName(favorite.getHotelName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        var response = new GetFavoriteHotelsResponse();
        response.setFavoriteHotels(favoriteCities);
        return response;
    }
}
