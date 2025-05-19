package ru.ssau.lab6.model;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
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
@Table("habits_by_respondent")
public class Habits
{
    @IgnoreEdit
    @TableHeader(name = "Идентификатор")
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    private UUID id;

    @TableHeader(name = "Идентификатор опрошенного")
    @PrimaryKeyColumn(name = "respondent_id", type = PrimaryKeyType.PARTITIONED)
    private UUID respondentId;

    @TableHeader(name = "Среднее количество часов сна в сутки")
    @Column("sleep_hours")
    private Double sleepHours;

    @TableHeader(name = "Среднее количество дней в неделю с физической активностью")
    @Column("physical_activity_days")
    private Integer physicalActivityDays;
    
    @TableHeader(name = "Частота социальных взаимодействий")
    @Column("social_interaction_freq")
    private ActivityFrequency socialInteractionFreq;

    @TableHeader(name = "Дата опроса")
    @IgnoreEdit
    @Column("survey_date")
    private LocalDate surveyDate = LocalDate.now(); 
}
