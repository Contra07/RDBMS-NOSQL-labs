package ru.ssau.lab6.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.cassandra.repository.MapIdCassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import ru.ssau.lab6.model.Habits;

@Repository
public interface HabitsRepository extends MapIdCassandraRepository<Habits>
{
    @Query("SELECT * FROM habits_by_respondent WHERE survey_date >= :date ALLOW FILTERING")
    List<Habits> findByGreaterDate(LocalDate date);

    @Query("SELECT * FROM habits_by_respondent WHERE survey_date = :date")
    List<Habits> findByDate(LocalDate date);
}
