package ru.ifmo.hotels.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.ifmo.common.dto.hotels.GetHotelsRequest;
import ru.ifmo.common.dto.hotels.GetHotelsResponse;
import ru.ifmo.common.dto.hotels.HotelDto;
import ru.ifmo.hotels.domain.entity.City;
import ru.ifmo.hotels.domain.entity.Hotel;
import ru.ifmo.hotels.domain.entity.Tariff;
import ru.ifmo.hotels.domain.repository.CityRepository;
import ru.ifmo.hotels.domain.repository.HotelRepository;
import ru.ifmo.hotels.exception.HotelsException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class HotelsService {

    private final HotelRepository hotelRepository;
    private final NumbersService numbersService;
    private final TariffsService tariffsService;
    private final CityRepository cityRepository;

    public List<HotelDto> getAllByNameIsLike(String destinationName) {
        return hotelRepository.findAllByNameIsLike(destinationName);
    }

    public Optional<HotelDto> findByHotelName(String hotelName) {
        return hotelRepository.findByName(hotelName)
                .map(this::toDto);
    }

    public Page<GetHotelsResponse> findAllHotels(GetHotelsRequest request, PageRequest pageable) {
        var availableHotels = hotelRepository.findAvailableHotels(request, pageable);
        var hotelModels = availableHotels
                .stream()
                .map(hotel -> toModel(hotel, request))
                .toList();
        return new PageImpl<>(hotelModels, pageable, availableHotels.getTotalElements());
    }

    private HotelDto toDto(Hotel hotel) {
        var city = cityRepository.findById(hotel.getCityId())
                .orElseThrow(() -> new HotelsException("City wasnt found", HttpStatus.NOT_FOUND))
                .getName();
        return new HotelDto(city, hotel.getName());
    }

    private GetHotelsResponse toModel(Hotel hotel, GetHotelsRequest request) {
        var availableNumber = numbersService.findAllByHotelId(hotel.getId())
                .stream()
                .filter(number -> number.getMaxOccupancy() >= request.getGuestsCount())
                .findFirst()
                .orElseThrow(() -> new HotelsException(
                        String.format(
                                "Numbers of hotel: %s doesn't have enough capacity for: %s guests",
                                hotel.getName(), request.getGuestsCount()
                        ),
                        HttpStatus.CONFLICT
                ));
        var cheapestTariff = tariffsService.findAllByNumberId(availableNumber.getId())
                .stream()
                .min(Comparator.comparingDouble(Tariff::getPrice))
                .orElseThrow(() -> new HotelsException(
                        String.format(
                                "No available tariff for hotel: %s guests",
                                request.getGuestsCount()
                        ),
                        HttpStatus.CONFLICT
                ));

        return GetHotelsResponse.builder()
                .name(hotel.getName())
                .rating(hotel.getRating())
                .pricePerDay(cheapestTariff.getPrice().intValue())
                .totalPrice(countTotalPrice(cheapestTariff, request))
                .distanceFromCenter(hotel.getDistanceFromCenter())
                .build();
    }

    private Integer countTotalPrice(Tariff cheapestTariff, GetHotelsRequest request) {
        var startDate = request.getStartBookingDate();
        var endDate = request.getEndBookingDate();
        var daysCount = (int) DAYS.between(startDate, endDate);
        return cheapestTariff.getPrice().intValue() * daysCount;
    }
}
