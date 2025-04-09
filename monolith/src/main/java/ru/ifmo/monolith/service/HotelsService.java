package ru.ifmo.monolith.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.ifmo.monolith.domain.entity.Hotel;
import ru.ifmo.monolith.domain.entity.Tariff;
import ru.ifmo.monolith.domain.repository.HotelRepository;
import ru.ifmo.monolith.dto.GetHotelsRequest;
import ru.ifmo.monolith.dto.GetHotelsResponse;
import ru.ifmo.monolith.dto.HotelDto;
import ru.ifmo.monolith.exception.MonolithException;

import java.util.Comparator;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class HotelsService {

    private final HotelRepository hotelRepository;
    private final NumbersService numbersService;
    private final TariffsService tariffsService;

    public List<HotelDto> getAllByNameIsLike(String destinationName) {
        return hotelRepository.findAllByNameIsLike(destinationName);
    }

    public Page<GetHotelsResponse> findAllHotels(GetHotelsRequest request, PageRequest pageable) {
        var availableHotels = hotelRepository.findAvailableHotels(request, pageable);
        var hotelModels = availableHotels
                .stream()
                .map(hotel -> toModel(hotel, request))
                .toList();
        return new PageImpl<>(hotelModels, pageable, availableHotels.getTotalElements());
    }

    private GetHotelsResponse toModel(Hotel hotel, GetHotelsRequest request) {
        var availableNumber = numbersService.findAllByHotelId(hotel.getId())
                .stream()
                .filter(number -> number.getMaxOccupancy() >= request.getGuestsCount())
                .findFirst()
                .orElseThrow(() -> new MonolithException(
                        String.format(
                                "Numbers of hotel: %s doesn't have enough capacity for: %s guests",
                                hotel.getName(), request.getGuestsCount()
                        ),
                        HttpStatus.CONFLICT
                ));
        var cheapestTariff = tariffsService.findAllByNumberId(availableNumber.getId())
                .stream()
                .min(Comparator.comparingDouble(Tariff::getPrice))
                .orElseThrow(() -> new MonolithException(
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
