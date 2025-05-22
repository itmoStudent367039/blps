package ru.ifmo.hotels.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.hotels.domain.entity.TariffExcludeOption;
import java.util.List;

@Repository
// todo: jpa specification
public interface TariffExcludeOptionRepository extends JpaRepository<TariffExcludeOption, Integer> {

    @Query("FROM TariffExcludeOption t WHERE t.tariffOptionId.tariffId = :id")
    List<TariffExcludeOption> findAllByTariffId(@Param("id") Integer tariffId);
}
