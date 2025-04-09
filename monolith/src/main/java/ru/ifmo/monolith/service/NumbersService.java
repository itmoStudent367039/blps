package ru.ifmo.monolith.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ifmo.monolith.domain.entity.Number;
import ru.ifmo.monolith.domain.entity.Tariff;
import ru.ifmo.monolith.domain.entity.TariffOption;
import ru.ifmo.monolith.domain.repository.NumbersRepository;
import ru.ifmo.monolith.domain.repository.TariffExcludeOptionRepository;
import ru.ifmo.monolith.domain.repository.TariffIncludeOptionRepository;
import ru.ifmo.monolith.domain.repository.TariffOptionRepository;
import ru.ifmo.monolith.dto.GetAvailableNumbersRequest;
import ru.ifmo.monolith.dto.NumberDto;
import ru.ifmo.monolith.dto.TariffDto;
import ru.ifmo.monolith.dto.TariffOptionDto;

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
