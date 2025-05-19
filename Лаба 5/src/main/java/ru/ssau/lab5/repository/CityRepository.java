package ru.ssau.lab5.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab5.model.City;
import ru.ssau.lab5.model.CityNameAndAnxietyLevel;
import ru.ssau.lab5.model.CityNameAndCount;

@Repository
@RequiredArgsConstructor
public class CityRepository 
{
    private final Neo4jClient client;
    private final Neo4jMappingContext mappingContext;

    public Collection<City> findAll()
    {
        return client
            .query("MATCH (c:City) RETURN c")
            .fetchAs(City.class)
            .mappedBy((t, record) -> mappingContext.getRequiredMappingFunctionFor(City.class).apply(t, record.get("c")))
            .all();
    }

    public Collection<CityNameAndCount> findByCount(int personCount)
    {
        return client
            .query("""
                MATCH (p:Person)-[:lives_in]->(c:City)
                WITH count(p) as person_count, c.name as city_name
                WHERE person_count >= $count
                return person_count, city_name
            """)
            .bind(personCount).to("count")
            .fetchAs(CityNameAndCount.class)
            .mappedBy((t, record) -> new CityNameAndCount(record.get("person_count").asInt(), record.get("city_name").asString()))
            .all();
    }

    public Collection<CityNameAndAnxietyLevel> findByAnxietyLevel(double anxietyLevel)
    {
        return client
            .query("""
                MATCH (s:Survey)<-[:Participated]-(p:Person)-[:lives_in]->(c:City)
                with c.name as city_name, avg(s.anxiety_level) as avg_anxiety_level
                where avg_anxiety_level <= $anxiety_level
                return city_name, avg_anxiety_level
            """)
            .bind(anxietyLevel).to("anxiety_level")
            .fetchAs(CityNameAndAnxietyLevel.class)
            .mappedBy((t, record) -> new CityNameAndAnxietyLevel(record.get("avg_anxiety_level").asDouble(), record.get("city_name").asString()))
            .all();
    }

    public Optional<City> findByName(String name)
    {
        return client
            .query("""
                MATCH (p:Person)-[:lives_in]->(c:City)
                WHERE c.name = $name
                RETURN c
            """)
            .bind(name).to("name")
            .fetchAs(City.class)
            .mappedBy((t, record) -> mappingContext.getRequiredMappingFunctionFor(City.class).apply(t, record.get("c")))
            .one();
    }

    public int merge(City city)
    {
        var summary = client
            .query("""
                MERGE (
                    c:City {name: $name}
                )
                SET c.is_capital = $is_capital,
                    c.noise_pollution_level = $noise_pollution_level,
                    c.access_to_nature = $access_to_nature
            """)
            .bind(city.getName()).to("name")
            .bind(city.getIsCapital()).to("is_capital")
            .bind(city.getNoisePollutionLevel()).to("noise_pollution_level")
            .bind(city.getAccessToNature()).to("access_to_nature")
            .run();

        return summary.counters().nodesCreated();
    }

    public Optional<City> set(String name, City city)
    {
        return client
            .query("""
                MATCH (
                    c:City {name: $old_name}
                )
                SET c.name = $new_name,
                    c.is_capital = $is_capital,
                    c.noise_pollution_level = $noise_pollution_level,
                    c.access_to_nature = $access_to_nature
                RETURN c
            """)
            .bind(name).to("old_name")
            .bind(city.getName()).to("new_name")
            .bind(city.getIsCapital()).to("is_capital")
            .bind(city.getNoisePollutionLevel()).to("noise_pollution_level")
            .bind(city.getAccessToNature()).to("access_to_nature")
            .fetchAs(City.class)
            .mappedBy((t, record) -> mappingContext.getRequiredMappingFunctionFor(City.class).apply(t, record.get("c")))
            .one();
    }

    public int delete(String name)
    {
        var nodesDeleted = client
            .query("""
                MATCH (
                    c:City {name: $name}
                )
                DETACH DELETE c
            """)
            .bind(name).to("name")
            .run()
            .counters()
            .nodesDeleted();

        return nodesDeleted;
    }
}