package ru.ssau.lab2.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import ru.ssau.lab2.model.DemographicsReplacing;

public interface DemographicsReplacingRepository  extends Repository<DemographicsReplacing, Integer>
{
    @Query("""
        
        select * 
        from demographics_replacing 
        order by id
    """)
    List<DemographicsReplacing> findAll();

    @Query("""

        select * from 
        demographics_replacing final 
        order by id
    """)
    List<DemographicsReplacing> findAllFinal();

    @Query("""

        SELECT demographics_replacing.*
        from demographics_replacing as dr
        order by dr.version desc
        limit 1 by dr.id
    """)
    List<DemographicsReplacing> findAllReplacing();

    @Query("""

        select * 
        from demographics_replacing 
        where id = :id 
        order by id
    """)
    List<DemographicsReplacing> findById(@Param("id") Integer id);

    @Query("""

        select * 
        from demographics_replacing final 
        where id = :id 
        order by id
    """)
    List<DemographicsReplacing> findByIdFinal(@Param("id") Integer id);

    @Query("""

        with maxVersion as
        (
            SELECT id, max(version) as version
            FROM demographics_replacing
            where id = :id
            GROUP BY id
        )
        SELECT demographics_replacing.*
        from demographics_replacing as dr join maxVersion as mv on mv.id = dr.id
        WHERE mv.version = dr.version and dr.deleted = 0     
    """)
    List<DemographicsReplacing> findByIdReplacing(@Param("id") Integer id);

    @Query(value = """

        INSERT INTO demographics_replacing (*)
        SELECT 
            (SELECT MAX(id)+1 as id FROM demographics_replacing), 
            :#{#demographicsReplacing.age}, 
            :#{#demographicsReplacing.gender.toString()}, 
            :#{#demographicsReplacing.occupation}, 
            :#{#demographicsReplacing.location}, 
            :#{#demographicsReplacing.registrationDate}, 
            1,
            0
    """)
    void insert(@Param("demographicsReplacing") DemographicsReplacing demographicsReplacing);

    @Query(value = """

        DELETE demographics_replacing 
        WHERE id = :id
    """)
    void delete(@Param("id") Integer id);

    @Query(value = """

        ALTER TABLE demographics_replacing 
        DELETE WHERE id = :id
    """)
    void deleteAlter(@Param("id") Integer id);

    @Query(value = """

        with maxVersion as
        (
            SELECT id, max(version) as version
            FROM demographics_replacing
            where id = :id
            GROUP BY id
        )
        INSERT INTO demographics_replacing (id,version,deleted ) 
        SELECT mv.id, mv.version + 1, 1
        from demographics_replacing as dr join maxVersion as mv on mv.id = dr.id
        WHERE mv.version = dr.version and dr.deleted = 0
    """)
    void deleteReplacing(@Param("id") Integer id);

    @Query(value = """
        
        ALTER TABLE demographics_replacing UPDATE 
            age = :#{#demographicsReplacing.age},
            gender = :#{#demographicsReplacing.gender.toString()},
            location = :#{#demographicsReplacing.location},
            occupation = :#{#demographicsReplacing.occupation},
            registration_date = :#{#demographicsReplacing.registrationDate},
            version = :#{#demographicsReplacing.version},
            deleted = :#{#demographicsReplacing.deleted}
        WHERE id = :id
    """)
    void updateAlter(@Param("id") Integer id, @Param("demographicsReplacing") DemographicsReplacing demographics_replacing);

    @Query(value = """

        with maxVersion as
        (
            SELECT id, max(version) as version
            FROM demographics_replacing
            where id = :#{#demographicsReplacing.id}
            GROUP BY id
        )
        INSERT INTO demographics_replacing (*)
        SELECT 
            mv.id,
            :#{#demographicsReplacing.age}, 
            :#{#demographicsReplacing.gender.toString()}, 
            :#{#demographicsReplacing.occupation}, 
            :#{#demographicsReplacing.location}, 
            :#{#demographicsReplacing.registrationDate}, 
            mv.version + 1,
            0
        from maxVersion as mv
    """)
    void updateReplacing(@Param("id") Integer id, @Param("demographicsReplacing") DemographicsReplacing demographics_replacing);

    @Query(value = """

            OPTIMIZE TABLE demographics_replacing FINAL
    """)
    void optimize();
}
