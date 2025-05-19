package ru.ssau.lab6.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.repository.MapIdCassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import ru.ssau.lab6.model.Respondents;
import java.util.UUID;


public interface DemographicsRepository extends MapIdCassandraRepository<Respondents>
{
    List<Respondents> findAllByLocation(String location);

    @Query("SELECT * FROM respondents_by_location WHERE id = :id LIMIT 1")
    Optional<Respondents> findByRespondentId(UUID id);

    void deleteAllByLocation(String location);

    @Query("DELETE FROM respondents_by_location WHERE id = :id")
    void deleteAllByRespondentId(UUID id);
}