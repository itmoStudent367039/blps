package ru.ifmo.hotels.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.hotels.domain.entity.Favorite;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorite, Integer> {

    List<Favorite> findAllByUsername(String username);
}
