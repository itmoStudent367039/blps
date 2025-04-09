package ru.ifmo.monolith.domain.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ifmo.monolith.domain.entity.Tariff;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TariffRepositoryTest extends AbstractDatabaseTest {

    @Autowired
    TariffRepository tariffRepository;

    @Test
    void getPricePerDayTest() {
        assertThat(tariffRepository.getPricePerDay(
                "Hotel Moscow",
                "Standard Room",
                "Basic"))
                .isPresent()
                .get()
                .isEqualTo(50.);
    }

    @Test
    void getTariffByNamesTest() {
        Optional<Tariff> byNames = tariffRepository.findByNames(
                "Hotel Moscow",
                "Standard Room",
                "Basic");
        assertThat(byNames.isPresent()).isTrue();
        Tariff actual = byNames.get();
        assertThat(actual).hasFieldOrPropertyWithValue("tariffName", "Basic")
                .hasFieldOrPropertyWithValue("price", 50.);
    }
}