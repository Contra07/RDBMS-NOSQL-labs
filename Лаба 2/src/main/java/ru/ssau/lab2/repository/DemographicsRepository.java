package ru.ssau.lab2.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import ru.ssau.lab2.model.Demographics;
import java.util.List;

@org.springframework.stereotype.Repository
public interface DemographicsRepository extends Repository<Demographics, Integer>
{
    @Query("select * from demographics order by id")
    List<Demographics> findAll();

    @Query("select * from demographics where id = :id order by id")
    List<Demographics> findById(@Param("id") Integer id);

    @Query(value = """
        INSERT INTO demographics (*)
        SELECT (SELECT MAX(id)+1 as id FROM demographics), :#{#demographic.age}, :#{#demographic.gender.toString()}, :#{#demographic.occupation}, :#{#demographic.location}, :#{#demographic.registrationDate}
    """)
    void insert(@Param("demographic") Demographics demographics);

    @Query(value = """
        ALTER TABLE demographics DELETE WHERE id = :id
    """)
    void deleteAlter(@Param("id") Integer id);

    @Query(value = """
        DELETE demographics WHERE id = :id
    """)
    void delete(@Param("id") Integer id);

    @Query(value = """
        ALTER TABLE demographics UPDATE 
            age = :#{#demographic.age},
            gender = :#{#demographic.gender.toString()},
            location = :#{#demographic.location},
            occupation = :#{#demographic.occupation},
            registration_date = :#{#demographic.registrationDate}
        WHERE id = :id
    """)
    void updateAlter(@Param("id") Integer id, @Param("demographic") Demographics demographics);
}
