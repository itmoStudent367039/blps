package ru.ifmo.hotels.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.common.dto.hotels.DestinationsResponse;
import ru.ifmo.hotels.exception.HotelsException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class DestinationsService {

    private final CitiesService citiesService;
    private final HotelsService hotelsService;

    public DestinationsResponse getAllByName(String destinationName) {
        var citiesByName = citiesService.getAllByNameIsLike(destinationName);
        var hotelsByName = hotelsService.getAllByNameIsLike(destinationName);
        if (isEmpty(citiesByName) || isEmpty(hotelsByName)) {
            throw new HotelsException(
                    String.format("Destinations weren't found by name: %s", destinationName),
                    NOT_FOUND);
        }
        return new DestinationsResponse(citiesByName, hotelsByName);
    }
}
