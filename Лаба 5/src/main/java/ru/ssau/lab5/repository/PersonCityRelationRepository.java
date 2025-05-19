package ru.ssau.lab5.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab5.model.PersonAndCityRelationShip;

@Repository
@RequiredArgsConstructor
public class PersonCityRelationRepository 
{
    private final Neo4jClient client;

    public Collection<PersonAndCityRelationShip> findAll()
    {
        return client
            .query("""
                MATCH (p:Person)-[r:lives_in]-(c:City)
                RETURN p.name as person_name, r.date as date, c.name as city_name
            """)
            .fetchAs(PersonAndCityRelationShip.class)
            .mappedBy(
                (t, record) -> 
                {
                    return new PersonAndCityRelationShip(
                        record.get("person_name").asString(), 
                        record.get("city_name").asString(), 
                        record.get("date").asLocalDate()
                    );
                }
            )
            .all();
    } 

    public Collection<PersonAndCityRelationShip> findByDate(LocalDate date)
    {
        return client
            .query("""
                MATCH (p:Person)-[r:lives_in]-(c:City)
                where r.date >= $date
                RETURN p.name as person_name, r.date as date, c.name as city_name
            """)
            .bind(date).to("date")
            .fetchAs(PersonAndCityRelationShip.class)
            .mappedBy(
                (t, record) -> 
                {
                    return new PersonAndCityRelationShip(
                        record.get("person_name").asString(), 
                        record.get("city_name").asString(), 
                        record.get("date").asLocalDate()
                    );
                }
            )
            .all();
    }

    public Optional<PersonAndCityRelationShip> find(String cityName, String personName)
    {
        return client
            .query("""
                MATCH (p:Person { name: $person_name })-[r:lives_in]-(c:City { name: $city_name })
                RETURN p.name as person_name, r.date as date, c.name as city_name
            """)
            .bind(cityName).to("city_name")
            .bind(personName).to("person_name")
            .fetchAs(PersonAndCityRelationShip.class)
            .mappedBy(
                (t, record) -> 
                {
                    return new PersonAndCityRelationShip(
                        record.get("person_name").asString(), 
                        record.get("city_name").asString(), 
                        record.get("date").asLocalDate()
                    );
                }
            )
            .first();
    }

    public int merge(PersonAndCityRelationShip relationShip)
    {
        var summary = client
            .query("""
                MERGE (
                    p:Person { name: $person_name }
                )
                MERGE (
                    c:City { name: $city_name }
                )
                MERGE 
                    (p)-[
                        r:lives_in { date:datetime() }
                    ]->(c)
            """)
            .bind(relationShip.getPersonName()).to("person_name")
            .bind(relationShip.getCityName()).to("city_name")
            .run();

        return summary.counters().relationshipsCreated();
    }

    public Optional<PersonAndCityRelationShip> set(String cityName, String personName, PersonAndCityRelationShip newPerson)
    {
        return client
            .query("""
                MATCH (p:Person { name: $old_person_name })-[r:lives_in]-(c:City { name: $old_city_name })
                SET p.name = $new_person_name,
                    c.name = $new_city_name
                RETURN p.name as person_name, r.date as date, c.name as city_name
            """)
            .bind(cityName).to("old_city_name")
            .bind(personName).to("old_person_name")
            .bind(newPerson.getCityName()).to("new_city_name")
            .bind(newPerson.getPersonName()).to("new_person_name")
            .fetchAs(PersonAndCityRelationShip.class)
            .mappedBy(
                (t, record) -> 
                {
                    return new PersonAndCityRelationShip(
                        record.get("person_name").asString(), 
                        record.get("city_name").asString(), 
                        record.get("date").asLocalDate()
                    );
                }
            )
            .one();
    }

    public int delete(PersonAndCityRelationShip relationShip)
    {
        var nodesDeleted = client
            .query("""
                MATCH (p:Person { name: $person_name })-[r:lives_in]-(c:City { name: $city_name })
                DELETE r
            """)
            .bind(relationShip.getCityName()).to("person_name")
            .bind(relationShip.getPersonName()).to("city_name")
            .run()
            .counters()
            .relationshipsDeleted();

        return nodesDeleted;
    }
}
