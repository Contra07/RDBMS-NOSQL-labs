package ru.ssau.lab6.model;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.IgnoreEdit;
import ru.ssau.shared.shell.TableHeader;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Table(value = "respondents_by_location")
public class Respondents
{
   
    @IgnoreEdit
    @TableHeader(name = "Идентификатор")
    @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.CLUSTERED)
    protected UUID id;
    
    @TableHeader(name = "Место жительства")
    @PrimaryKeyColumn(name = "location", type = PrimaryKeyType.PARTITIONED)
    private String location;

    @TableHeader(name = "Возраст")
    private Integer age;

    @TableHeader(name = "Пол")
    private Gender gender;

    @TableHeader(name = "Занятость")
    private String occupation;

    @IgnoreEdit
    @TableHeader(name = "Дата регистрации")
    @Column("registration_date")
    private LocalDate registrationDate = LocalDate.now();
}