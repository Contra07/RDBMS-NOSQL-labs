package ru.ssau.lab2.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import ru.ssau.lab2.model.AvgMentalStatsPerUser;
import ru.ssau.lab2.model.AvgMentalStatsPerYears;
import ru.ssau.lab2.model.Habits;
import ru.ssau.lab2.model.MentalStats;

public interface MentalStatsRepository extends Repository<MentalStats, Integer>
{
    @Query("select * from mental_stats order by id")
    List<MentalStats> findAll();

    @Query("select * from mental_stats where id = :id order by id")
    List<MentalStats> findById(@Param("id") Integer id);

    @Query("select * from mental_stats where user_id = :userId order by id")
    List<Habits> findByUserId(@Param("id") Integer userId);


    @Query(value = """

        INSERT INTO mental_stats (*)
        SELECT (SELECT MAX(id)+1 as id FROM mental_stats), :#{#mental_stats.userId}, :#{#mental_stats.stressLevel}, :#{#mental_stats.anxietyLevel}, :#{#mental_stats.depressionLevel}, :#{#mental_stats.date}
    """)
    void insert(@Param("mental_stats") MentalStats mental_stats);

    @Query(value = """

        ALTER TABLE mental_stats DELETE WHERE user_id = :userId
    """)
    void deleteAlterByUser(@Param("userId") Integer userId);

    @Query(value = """

        ALTER TABLE mental_stats DELETE WHERE id = :id
    """)
    void deleteAlter(@Param("id") Integer id);

    @Query(value = """

        DELETE mental_stats WHERE id = :id
    """)
    void delete(@Param("id") Integer id);

    @Query(value = """

        DELETE mental_stats WHERE user_id = :userId
    """)
    void deleteByUser(@Param("userId") Integer userId);

    @Query(value = """
    
        ALTER TABLE mental_stats UPDATE 
            user_id = :#{#mental_stats.userId},
            stress_level = :#{#mental_stats.stressLevel},
            anxiety_level = :#{#mental_stats.anxietyLevel},
            depression_level = :#{#mental_stats.depressionLevel},
            date = :#{#mental_stats.date}
        WHERE id = :id
    """)
    void updateAlter(@Param("id") Integer id, @Param("mental_stats") MentalStats mental_stats);

    @Query(value = """

        SELECT 
            d.id as id, 
            d.location, 
            d.occupation, 
            AVG(ms.stress_level) as stress_level, 
            avg(ms.anxiety_level) as anxiety_level, 
            avg(ms.depression_level) as depression_level
        FROM mental_stats ms
            right join demographics d on d.id = ms.user_id 
        GROUP by d.id, d.location, d.occupation
        order by d.id
    """)
    List<AvgMentalStatsPerUser> findAveragePerUser();

    @Query(value = """
        with firstYear as (
            SELECT 
                :user_id as id, 
                toMonth(ms.date) as month, 
                avg(ms.stress_level) as stress_level, 
                avg(ms.depression_level) as depression_level, 
                avg(ms.anxiety_level) as anxiety_level 
            from mental_stats ms 
            WHERE toYear(ms.date) = :first_year and ms.user_id = :user_id
            group by toMonth(ms.date) 
            order by toMonth(ms.date) 
        ),
        secondYear as (
            SELECT 
                :user_id as id, 
                toMonth(ms.date) as month, 
                avg(ms.stress_level) as stress_level, 
                avg(ms.depression_level) as depression_level, 
                avg(ms.anxiety_level) as anxiety_level 
                from mental_stats ms 
            WHERE toYear(ms.date) = :second_year and ms.user_id = :user_id
            group by toMonth(ms.date) 
            order by toMonth(ms.date) 
        )
        SELECT 
            case
                when firstYear.month = 0 then secondYear.month
                else firstYear.month
            end as month,
            firstYear.stress_level as first_stress_level, 
            firstYear.depression_level as first_depression_level, 
            firstYear.anxiety_level as first_anxiety_level,
            secondYear.stress_level as second_stress_level, 
            secondYear.depression_level as second_depression_level, 
            secondYear.anxiety_level as second_anxiety_level
        from firstYear
            full join secondYear 
                on firstYear.id = secondYear.id 
                and firstYear.month = secondYear.month  
        order by month  
    """)
    List<AvgMentalStatsPerYears> findAveragePerYear(@Param("user_id") Integer userId, @Param("first_year") Integer firstYear, @Param("second_year") Integer secondYear);
}
