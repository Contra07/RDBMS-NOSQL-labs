package ru.ssau.lab5.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab5.model.Survey;


@Repository
@RequiredArgsConstructor
public class SurveyRepository
{
    private final Neo4jClient client;
    private final Neo4jMappingContext mappingContext;

    public Collection<Survey> findAll()
    {
        return client
            .query("MATCH (s:Survey) RETURN s")
            .fetchAs(Survey.class)
            .mappedBy((t, record) -> mappingContext.getRequiredMappingFunctionFor(Survey.class).apply(t, record.get("s")))
            .all();
    }

    public Optional<Survey> findById(int id)
    {
        return client
            .query("MATCH (s:Survey {id : $id}) RETURN s")
            .bind(id).to("id")
            .fetchAs(Survey.class)
            .mappedBy((t, record) -> mappingContext.getRequiredMappingFunctionFor(Survey.class).apply(t, record.get("s")))
            .one();
    }

    public int mergeWithName(String personName, Survey survey)
    {
        survey.setId(
            client
                .query("""
                    MATCH (s:Seq {key:"my_seq"})
                    SET s.value = s.value + 1
                    RETURN s.value     
                """)
                .fetchAs(Integer.class)
                .one()
                .get()
        );

        var summary = client
            .query("""
                MERGE (
                    p:Person { name: $person_name }
                )
                MERGE (
                    s:Survey { id: $id }
                )
                MERGE 
                    (p)-[
                        r:Participated {date: datetime()}
                    ]-(s)
                SET s.stress_level = $stress_level,
                    s.anxiety_level = $anxiety_level,
                    s.depression_level = $depression_level
            """)
            .bind(personName).to("person_name")
            .bind(survey.getId()).to("id")
            .bind(survey.getStressLevel()).to("stress_level")
            .bind(survey.getAnxietyLevel()).to("anxiety_level")
            .bind(survey.getDepressionLevel()).to("depression_level")
            .run();

        return summary.counters().nodesCreated();
    }

    public int merge(Survey survey)
    {
        survey.setId(
            client
                .query("""
                    MATCH (s:Seq {key:"my_seq"})
                    SET s.value = s.value + 1
                    RETURN s.value     
                """)
                .fetchAs(Integer.class)
                .one()
                .get()
        );

        var summary = client
            .query("""
                MERGE (
                    s:Survey {id: $id}
                )
                SET s.stress_level = $stress_level,
                    s.anxiety_level = $anxiety_level,
                    s.depression_level = $depression_level
            """)
            .bind(survey.getId()).to("id")
            .bind(survey.getStressLevel()).to("stress_level")
            .bind(survey.getAnxietyLevel()).to("anxiety_level")
            .bind(survey.getDepressionLevel()).to("depression_level")
            .run();

        return summary.counters().nodesCreated();
    }

    public Optional<Survey> set(Survey survey)
    {
        return client
            .query("""
                MATCH (
                    s:Survey {id: $id}
                )
                SET s.stress_level = $stress_level,
                    s.anxiety_level = $anxiety_level,
                    s.depression_level = $depression_level
                RETURN s
            """)
            .bind(survey.getId()).to("id")
            .bind(survey.getStressLevel()).to("stress_level")
            .bind(survey.getAnxietyLevel()).to("anxiety_level")
            .bind(survey.getDepressionLevel()).to("depression_level")
            .fetchAs(Survey.class)
            .mappedBy((t, record) -> mappingContext.getRequiredMappingFunctionFor(Survey.class).apply(t, record.get("s")))
            .one();
    }

    public int delete(Integer id)
    {
        var nodesDeleted = client
            .query("""
                MATCH (
                    s:Survey {name: $id}
                )
                DETACH DELETE s
            """)
            .bind(id).to("id")
            .run()
            .counters()
            .nodesDeleted();

        return nodesDeleted;
    }
}
