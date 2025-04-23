package ru.ifmo.hotels.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.hotels.domain.entity.TariffOption;

@Repository
// todo: jpa specification
public interface TariffOptionRepository extends JpaRepository<TariffOption, Integer> {

}
