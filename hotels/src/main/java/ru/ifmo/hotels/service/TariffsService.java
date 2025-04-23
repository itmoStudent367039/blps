package ru.ifmo.hotels.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.hotels.domain.entity.Tariff;
import ru.ifmo.hotels.domain.repository.TariffRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TariffsService {

    private final TariffRepository tariffRepository;

    public Optional<Double> getPricePerDay(String hotelName, String numberName, String tariffName) {
        return tariffRepository.getPricePerDay(hotelName, numberName, tariffName);
    }

    public Optional<Tariff> findByNames(String hotelName, String numberName, String tariffName) {
        return tariffRepository.findByNames(hotelName, numberName, tariffName);
    }

    public List<Tariff> findAllByNumberId(Integer numberId) {
        return tariffRepository.findAllByNumberId(numberId);
    }
}
