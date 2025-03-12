package ru.ifmo.monolith.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.monolith.domain.repository.CityRepository;
import ru.ifmo.monolith.dto.CityDto;

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
