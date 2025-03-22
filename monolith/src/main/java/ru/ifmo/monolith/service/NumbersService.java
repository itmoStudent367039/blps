package ru.ifmo.monolith.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.ifmo.monolith.domain.entity.Number;
import ru.ifmo.monolith.domain.entity.Tariff;
import ru.ifmo.monolith.domain.entity.TariffOption;
import ru.ifmo.monolith.domain.repository.HotelRepository;
import ru.ifmo.monolith.domain.repository.NumbersRepository;
import ru.ifmo.monolith.dto.GetAvailableNumbersRequest;
import ru.ifmo.monolith.dto.NumberDto;
import ru.ifmo.monolith.dto.TariffDto;
import ru.ifmo.monolith.dto.TariffOptionDto;


@Service
@RequiredArgsConstructor
public class NumbersService {

    private final HotelRepository hotelRepository;
    private final NumbersRepository numbersRepository;

    public Page<NumberDto> findAllByHotelName(GetAvailableNumbersRequest request,
                                              PageRequest pageable) {
        var entityNumbers = numbersRepository.findAvailableNumbers(request, pageable);
        var modelNumbers = entityNumbers.stream()
                .map(this::toModel)
                .toList();
        return new PageImpl<>(modelNumbers, pageable, entityNumbers.getTotalElements());
    }

    private NumberDto toModel(Number number) {
        var tariffs = number.getTariffs()
                .stream()
                .map(this::toModel)
                .toList();
        return NumberDto.builder()
                .tariffs(tariffs)
                .name(number.getName())
                .maxOccupancy(number.getMaxOccupancy())
                .numberOfRooms(number.getNumberOfRooms())
                .numberOfPairBeds(number.getNumberOfPairBeds())
                .numberOfSingleBeds(number.getNumberOfSingleBeds())
                .build();
    }

    private TariffDto toModel(Tariff tariff) {
        var includeOptions = tariff.getIncludeOptions()
                .stream()
                .map(this::toModel)
                .toList();
        var excludeOptions = tariff.getExcludeOptions()
                .stream()
                .map(this::toModel)
                .toList();
        return TariffDto.builder()
                .tariffName(tariff.getTariffName())
                .price(tariff.getPrice())
                .excludeOptions(excludeOptions)
                .includeOptions(includeOptions)
                .build();
    }

    private TariffOptionDto toModel(TariffOption tariffOption) {
        return TariffOptionDto.builder()
                .optionName(tariffOption.getOptionName())
                .build();
    }
}
