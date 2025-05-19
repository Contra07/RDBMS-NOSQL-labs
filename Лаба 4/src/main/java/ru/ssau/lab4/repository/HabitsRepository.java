package ru.ssau.lab4.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import ru.ssau.lab4.model.Habits;

public interface HabitsRepository extends ListCrudRepository<Habits, String> 
{
    @Query(delete = true, value = "{'userId': ?#{[0]} }")
    void deleteByUser(String userId);

    @Query(
        """
        { 
            $and: 
            [
                { 'year': { $gte: ?0, $lte: ?1 } },
                { 'physicalActivityDays': { $gt: ?2 } }
            ] 
        }
        """
    )
    List<Habits> findTask(int leftYear, int rightYear, double physicalActivityDays);
}
