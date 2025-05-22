package ru.ifmo.hotels.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.common.dto.hotels.CityDto;
import ru.ifmo.hotels.domain.repository.CityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitiesService {

    private final CityRepository cityRepository;

    public List<CityDto> getFavorites() {
        return cityRepository.findCityNameAndHotelCountList();
    }

    public List<CityDto> getAllByNameIsLike(String destinationName) {
        return cityRepository.findAllByNameIsLike(destinationName);
    }
}
