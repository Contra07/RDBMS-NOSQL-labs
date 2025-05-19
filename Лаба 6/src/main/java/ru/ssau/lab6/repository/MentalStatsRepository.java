package ru.ssau.lab6.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.cassandra.repository.MapIdCassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import ru.ssau.lab6.model.Habits;
import ru.ssau.lab6.model.MentalStats;

public interface MentalStatsRepository extends MapIdCassandraRepository<MentalStats>
{
    @Query("SELECT * FROM mental_stats_by_respondent WHERE survey_date >= :date ALLOW FILTERING")
    List<Habits> findByGreaterDate(LocalDate date);

    @Query("SELECT * FROM mental_stats_by_respondent WHERE survey_date = :date")
    List<Habits> findByDate(LocalDate date);
}
