package ru.ifmo.monolith.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.monolith.domain.entity.TariffIncludeOption;

import java.util.List;

@Repository
// todo: jpa specification
public interface TariffIncludeOptionRepository extends JpaRepository<TariffIncludeOption, Integer> {

    @Query("FROM TariffIncludeOption t WHERE t.tariffOptionId.tariffId = :id")
    List<TariffIncludeOption> findAllByTariffId(@Param("id") Integer tariffId);
}
