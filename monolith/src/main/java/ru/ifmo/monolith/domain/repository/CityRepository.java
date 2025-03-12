package ru.ifmo.monolith.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.monolith.domain.entity.City;
import ru.ifmo.monolith.dto.CityDto;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, String> {

    @Query("SELECT new ru.ifmo.monolith.dto.CityDto(c.name, COUNT(h)) " +
            "FROM Hotel h " +
            "JOIN h.city c " +
            "GROUP BY c.name " +
            "ORDER BY COUNT(h) DESC " +
            "LIMIT 5")
    List<CityDto> findCityNameAndHotelCountList();

    @Query("SELECT new ru.ifmo.monolith.dto.CityDto(c.name, COUNT(h)) " +
            "FROM Hotel h " +
            "JOIN h.city c " +
            "WHERE c.name LIKE CONCAT('%', :name, '%') " +
            "GROUP BY c.name " +
            "ORDER BY COUNT(h) DESC " +
            "LIMIT 5")
    List<CityDto> findAllByNameIsLike(@Param("name") String name);
}
