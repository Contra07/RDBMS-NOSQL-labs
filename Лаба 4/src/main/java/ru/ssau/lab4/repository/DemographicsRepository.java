package ru.ssau.lab4.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.ListCrudRepository;

import ru.ssau.lab4.model.Demographics;

public interface DemographicsRepository extends ListCrudRepository<Demographics, String> 
{ 
    @Query("{ 'location' : ?0 }")
    @Update("{ 'location' : ?1 }")
    void updateByLocation(String oldLocation, String newLocation);
}
