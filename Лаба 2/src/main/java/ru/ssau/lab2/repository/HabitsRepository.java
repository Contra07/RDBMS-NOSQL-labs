package ru.ssau.lab2.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import ru.ssau.lab2.model.AvgHabitsPerYear;
import ru.ssau.lab2.model.Habits;
import ru.ssau.lab2.model.OccupationAndSleep;
import ru.ssau.lab2.model.OccupationAndSleepPerYear;

@org.springframework.stereotype.Repository
public interface HabitsRepository extends Repository<Habits, Integer>
{
    @Query("select * from habits order by id")
    List<Habits> findAll();

    @Query("select * from habits where id = :id order by id")
    List<Habits> findById(@Param("id") Integer id);

    @Query("select * from habits where user_id = :userId order by id")
    List<Habits> findByUserId(@Param("userId") Integer userId);

    @Query(value = """
        INSERT INTO habits (*)
        SELECT (SELECT MAX(id)+1 as id FROM habits), :#{#habits.userId}, :#{#habits.sleepHours}, :#{#habits.physicalActivityDays}, :#{#habits.socialInteractionFreq.toString()}, :#{#habits.year}
    """)
    void insert(@Param("habits") Habits habits);

    @Query(value = """
        ALTER TABLE habits DELETE WHERE id = :id
    """)
    void deleteAlter(@Param("id") Integer id);

    @Query(value = """
        ALTER TABLE habits DELETE WHERE user_id = :userId
    """)
    void deleteAlterByUser(@Param("userId") Integer userId);

    @Query(value = """
        DELETE habits WHERE id = :id
    """)
    void delete(@Param("id") Integer id);

    @Query(value = """
        DELETE habits WHERE user_id = :userId
    """)
    void deleteByUser(@Param("userId") Integer userId);

    @Query(value = """
        ALTER TABLE habits UPDATE 
            user_id = :#{#habits.userId},
            sleep_hours = :#{#habits.sleepHours},
            physical_activity_days = :#{#habits.physicalActivityDays},
            social_interaction_freq = :#{#habits.socialInteractionFreq.toString()},
            year = :#{#habits.year}
        WHERE id = :id
    """)
    void updateAlter(@Param("id") Integer id, @Param("habits") Habits habits);

    @Query(value = """

        SELECT 
            h.year as year, 
            d.occupation as occupation, 
            avg(h.sleep_hours) as avg_sleep_hours
        from demographics d 
            join habits h on h.user_id = d.id
        GROUP by d.occupation, h.year
        Order by h.year, d.occupation
    """)
    List<OccupationAndSleepPerYear> findOccupationAndSleep();

    @Query(value = """

        with s as
        (
            SELECT 
                d.occupation, 
                avg(h.sleep_hours) as avg_sleep_hours
            from demographics d 
                join habits h on h.user_id = d.id and h.year = :year
            GROUP by d.occupation
            Order by d.occupation
        )
        SELECT d.occupation, s.avg_sleep_hours
        from demographics d
            left join s as s on d.occupation = s.occupation
        GROUP by d.occupation, s.avg_sleep_hours
        Order by d.occupation
    """)
    List<OccupationAndSleep> findOccupationAndSleepByYear(@Param("year") int year);

    @Query(value = """

        with all_years as
        (
            SELECT range(min(h.year), max(h.year)) as years
            FROM habits h
        ),
        years as 
        (
            SELECT year
            FROM all_years
                ARRAY JOIN all_years.years as year
        ),
        allD as
        (
            select d.*, y.year  as year
            FROM demographics d
                cross join years as y
            WHERE d.id = :id
        )
        select 
            ad.year as year,
            avg(h.sleep_hours) AS avg_sleep_hours, 
            avg(h.physical_activity_days) as avg_physical_activity_days
        from habits h
            right join allD ad on h.user_id = ad.id and ad.year = h.year
        group by ad.year
        order by ad.year
    """)
    List<AvgHabitsPerYear> findAvgHabitsByUser(@Param("id") Integer id);
}
