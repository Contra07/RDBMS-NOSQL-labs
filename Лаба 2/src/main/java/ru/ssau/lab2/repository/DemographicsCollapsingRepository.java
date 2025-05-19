package ru.ssau.lab2.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import ru.ssau.lab2.model.DemographicsCollapsing;

/*
Enum как BIGINT если что
*/
public interface DemographicsCollapsingRepository extends Repository<DemographicsCollapsing, Integer>
{
    @Query("""
        
        select * 
        from demographics_collapsing 
        order by id
    """)
    List<DemographicsCollapsing> findAll();

    @Query("""

        select * from 
        demographics_collapsing final 
        order by id
    """)
    List<DemographicsCollapsing> findAllFinal();

    @Query("""

        SELECT id 
        from demographics_collapsing 
        GROUP BY id 
        HAVING sum(Sign) > 0
    """)
    List<Integer> findAllIdsCollapsing();

    @Query("""

        select * 
        from demographics_collapsing 
        where id = :id 
        order by id
    """)
    List<DemographicsCollapsing> findById(@Param("id") Integer id);

    @Query("""

        select * 
        from demographics_collapsing final 
        where id = :id 
        order by id
    """)
    List<DemographicsCollapsing> findByIdFinal(@Param("id") Integer id);

    @Query("""

        SELECT id 
        from demographics_collapsing 
        where id = :id 
        GROUP BY id 
        HAVING sum(Sign) > 0        
    """)
    List<Integer> findIdsByIdCollapsing(@Param("id") Integer id);

    @Query(value = """
        with maxId as 
        (
            SELECT MAX(dc.id) as id 
            FROM demographics_collapsing as dc
            GROUP BY dc.id
            HAVING sum(dc.Sign) > 0
        )
        INSERT INTO demographics_collapsing (*)
        SELECT 
            CASE 
                when MAX(maxId.id) > 0 then MAX(maxId.id)+1
                else 1
            END, 
            :#{#demographicsCollapsing.age}, 
            :#{#demographicsCollapsing.gender.toString()}, 
            :#{#demographicsCollapsing.occupation}, 
            :#{#demographicsCollapsing.location}, 
            :#{#demographicsCollapsing.registrationDate}, 
            1
        from maxId
    """)
    void insert(@Param("demographicsCollapsing") DemographicsCollapsing demographicsCollapsing);

    @Query(value = """

        DELETE demographics_collapsing 
        WHERE id = :id
    """)
    void delete(@Param("id") Integer id);

    @Query(value = """

        ALTER TABLE demographics_collapsing 
        DELETE WHERE id = :id
    """)
    void deleteAlter(@Param("id") Integer id);

    @Query(value = """

        INSERT INTO demographics_collapsing (id, Sign)
        SELECT id, -1 
        from demographics_collapsing
        where id = :id
        GROUP BY id
        HAVING sum(Sign) > 0
    """)
    void deleteCollapsing(@Param("id") Integer id);

    @Query(value = """
        
        ALTER TABLE demographics_collapsing UPDATE 
            age = :#{#demographicsCollapsing.age},
            gender = :#{#demographicsCollapsing.gender.toString()},
            location = :#{#demographicsCollapsing.location},
            occupation = :#{#demographicsCollapsing.occupation},
            registration_date = :#{#demographicsCollapsing.registrationDate},
            sign = :#{#demographicsCollapsing.sign}
        WHERE id = :id
    """)
    void updateAlter(@Param("id") Integer id, @Param("demographicsCollapsing") DemographicsCollapsing demographics_collapsing);

    @Query(value = """

        INSERT INTO demographics_collapsing (*)
        SELECT 
            :#{#demographicsCollapsing.id},
            :#{#demographicsCollapsing.age}, 
            :#{#demographicsCollapsing.gender.toString()}, 
            :#{#demographicsCollapsing.occupation}, 
            :#{#demographicsCollapsing.location}, 
            :#{#demographicsCollapsing.registrationDate}, 
            1
    """)
    void updateCollapsing(@Param("id") Integer id, @Param("demographicsCollapsing") DemographicsCollapsing demographics_collapsing);

    @Query(value = """

            OPTIMIZE TABLE demographics_collapsing FINAL
    """)
    void optimize();
}
