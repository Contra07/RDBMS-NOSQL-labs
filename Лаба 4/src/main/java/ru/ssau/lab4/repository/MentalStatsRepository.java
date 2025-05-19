package ru.ssau.lab4.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import ru.ssau.lab4.model.MentalStats;

public interface MentalStatsRepository extends ListCrudRepository<MentalStats, String>
{
    @Query(delete = true, value = "{'userId': ?#{[0]} }")
    void deleteByUser(String userId);
}
