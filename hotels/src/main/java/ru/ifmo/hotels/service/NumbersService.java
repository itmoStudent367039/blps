package ru.ifmo.hotels.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ifmo.common.dto.hotels.GetAvailableNumbersRequest;
import ru.ifmo.common.dto.hotels.NumberDto;
import ru.ifmo.common.dto.hotels.TariffDto;
import ru.ifmo.common.dto.hotels.TariffOptionDto;
import ru.ifmo.hotels.domain.entity.Tariff;
import ru.ifmo.hotels.domain.entity.TariffOption;
import ru.ifmo.hotels.domain.repository.NumbersRepository;
import ru.ifmo.hotels.domain.repository.TariffExcludeOptionRepository;
import ru.ifmo.hotels.domain.repository.TariffIncludeOptionRepository;
import ru.ifmo.hotels.domain.repository.TariffOptionRepository;
import ru.ifmo.hotels.domain.entity.Number;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class NumbersService {

    private final NumbersRepository numbersRepository;
    private final TariffsService tariffsService;
    private final TariffExcludeOptionRepository tariffExcludeOptionRepository;
    private final TariffIncludeOptionRepository tariffIncludeOptionRepository;
    private final TariffOptionRepository tariffOptionRepository;

    public Page<NumberDto> findAllByHotelName(GetAvailableNumbersRequest request,
                                              PageRequest pageable) {
        var entityNumbers = numbersRepository.findAvailableNumbers(request, pageable);
        var modelNumbers = entityNumbers.stream()
                .map(this::toModel)
                .toList();
        return new PageImpl<>(modelNumbers, pageable, entityNumbers.getTotalElements());
    }

    public List<Number> findAllByHotelId(Integer hotelId) {
        return numbersRepository.findAllByHotelId(hotelId);
    }

    private NumberDto toModel(Number number) {
        var tariffs = tariffsService.findAllByNumberId(number.getId())
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
        var includeOptions = tariffIncludeOptionRepository.findAllByTariffId(tariff.getId())
                .stream()
                .map(inOpt -> tariffOptionRepository.findById(inOpt.getTariffOptionId().getOptionId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::toModel)
                .toList();
        var excludeOptions = tariffExcludeOptionRepository.findAllByTariffId(tariff.getId())
                .stream()
                .map(inOpt -> tariffOptionRepository.findById(inOpt.getTariffOptionId().getOptionId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
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
