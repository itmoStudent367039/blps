package ru.ifmo.monolith.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ifmo.monolith.domain.entity.City;
import ru.ifmo.monolith.dto.CityDto;

import java.util.List;

@Repository
// todo: jpa specification
public interface CityRepository extends JpaRepository<City, String> {

    @Query("""
            SELECT new ru.ifmo.monolith.dto.CityDto(c.name, CAST(COUNT(h) AS INTEGER))
            FROM Hotel h
            JOIN City c ON c.id = h.cityId
            GROUP BY c.name
            ORDER BY COUNT(h) DESC
            """)
    List<CityDto> findCityNameAndHotelCountList();

    @Query("""
            SELECT new ru.ifmo.monolith.dto.CityDto(c.name, CAST(COUNT(h) AS INTEGER))
            FROM Hotel h
            JOIN City c ON c.id = h.cityId
            WHERE c.name LIKE CONCAT('%', :name, '%')
            GROUP BY c.name
            ORDER BY COUNT(h) DESC
            """)
    List<CityDto> findAllByNameIsLike(@Param("name") String name);
}
