package ru.ssau.lab5.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab5.model.Person;
import ru.ssau.lab5.model.PersonWithCity;

@Repository
@RequiredArgsConstructor
public class PersonRepository 
{
    private final Neo4jClient client;
    private final Neo4jMappingContext mappingContext;

    public Collection<Person> findAll()
    {
        return client
            .query("MATCH (p:Person) RETURN p")
            .fetchAs(Person.class)
            .mappedBy((t, record) -> mappingContext.getRequiredMappingFunctionFor(Person.class).apply(t, record.get("p")))
            .all();
    }
    
    public Optional<Person> findByName(String name)
    {
        return client
            .query("MATCH (p:Person {name : $name}) RETURN p")
            .bind(name).to("name")
            .fetchAs(Person.class)
            .mappedBy((t, record) -> mappingContext.getRequiredMappingFunctionFor(Person.class).apply(t, record.get("p")))
            .one();
    }

    public int merge(Person person)
    {
        var summary = client
            .query("""
                MERGE (
                    p:Person {name: $name}
                )
                SET p.age = $age,
                    p.gender = $gender,
                    p.occupation = $occupation
            """)
            .bind(person.getName()).to("name")
            .bind(person.getGender().toString()).to("gender")
            .bind(person.getOccupation()).to("occupation")
            .bind(person.getAge()).to("age")
            .run();

        return summary.counters().nodesCreated();
    }

    public int mergeWithCity(PersonWithCity person)
    {
        var summary = client
            .query("""
                MERGE (
                    p:Person {name: $name}
                )
                MERGE (
                    c:City {name: $city_name}
                )
                MERGE 
                    (p)-[
                        r:lives_in { date: date() }
                    ]->(c)
                SET p.age = $age,
                    p.gender = $gender,
                    p.occupation = $occupation
            """)
            .bind(person.getName()).to("name")
            .bind(person.getGender().toString()).to("gender")
            .bind(person.getOccupation()).to("occupation")
            .bind(person.getAge()).to("age")
            .bind(person.getCityName()).to("city_name")
            .run();

        return summary.counters().nodesCreated();
    }

    public Optional<Person> set(Person person)
    {
        return client
            .query("""
                MATCH (
                    p:Person {name: $name}
                )
                SET p.age = $age,
                    p.gender = $gender,
                    p.occupation = $occupation
                RETURN p
            """)
            .bind(person.getName()).to("name")
            .bind(person.getGender().toString()).to("gender")
            .bind(person.getOccupation()).to("occupation")
            .bind(person.getAge()).to("age")
            .fetchAs(Person.class)
            .mappedBy((t, record) -> mappingContext.getRequiredMappingFunctionFor(Person.class).apply(t, record.get("p")))
            .one();
    }

    public int delete(String name)
    {
        var nodesDeleted = client
            .query("""
                MATCH (
                    (p:Person {name: $name})-[r:Participated]->(s:Survey)
                )
                DETACH DELETE s
            """)
            .bind(name).to("name")
            .run()
            .counters()
            .nodesDeleted();

        nodesDeleted += client
            .query("""
                MATCH (
                    p:Person {name: $name}
                )
                DETACH DELETE p
            """)
            .bind(name).to("name")
            .run()
            .counters()
            .nodesDeleted();

        return nodesDeleted;
    }
}