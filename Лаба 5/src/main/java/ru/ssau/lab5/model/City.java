package ru.ssau.lab5.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.IgnoreEdit;
import ru.ssau.shared.shell.TableHeader;

@Node("City")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City 
{
    @Id
    @GeneratedValue
    @IgnoreEdit
    private String id;

    @TableHeader(name = "Название")
    @Property("name")
    private String name;

    @TableHeader(name = "Столица")
    @Property("is_capital")
    private Boolean isCapital;

    @TableHeader(name = "Уровень шума (дБ)")
    @Property("noise_pollution_level")
    private Double noisePollutionLevel;

    @TableHeader(name = "Доля озеленения")
    @Property("access_to_nature")
    private Double accessToNature;
}
