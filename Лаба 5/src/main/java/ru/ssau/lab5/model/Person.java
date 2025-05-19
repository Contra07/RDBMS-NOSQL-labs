package ru.ssau.lab5.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.ssau.shared.shell.IgnoreEdit;
import ru.ssau.shared.shell.TableHeader;

@Node("Person")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Person 
{
    @RelationshipId
    @GeneratedValue
    @IgnoreEdit
    private String id;
    
    @TableHeader(name = "Имя")
    @Property("name")
	private String name;

    @Property("age")
    @TableHeader(name = "Возраст")
    private Integer age;

    @Property("gender")
    @TableHeader(name = "Пол")
    private Gender gender;

    @Property("occupation")
    @TableHeader(name = "Занятость")
    private String occupation;
}
