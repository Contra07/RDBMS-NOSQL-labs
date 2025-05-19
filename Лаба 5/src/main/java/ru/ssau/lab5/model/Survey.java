package ru.ssau.lab5.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.IgnoreEdit;
import ru.ssau.shared.shell.TableHeader;

@Node("Survey")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Survey 
{
    @RelationshipId
    @GeneratedValue
    @IgnoreEdit
    private String _id;

    @Property("id")
    @TableHeader(name = "Идентификатор")
    @IgnoreEdit
    private Integer id;

    @Property("stress_level")
    @TableHeader(name = "Уровень стресса (0-10)")
    private Integer stressLevel;

    @Property("anxiety_level")
    @TableHeader(name = "Уровень тревоги (0-10)")
    private Integer anxietyLevel;

    @Property("depression_level")
    @TableHeader(name = "Уровень депрессии (0-10)")
    private Integer depressionLevel;
}
