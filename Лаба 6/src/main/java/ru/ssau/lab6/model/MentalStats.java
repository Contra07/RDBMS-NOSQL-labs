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
@Table("mental_stats_by_respondent")
public class MentalStats 
{
    
    @IgnoreEdit
    @TableHeader(name = "Идентификатор")
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    private UUID id;
    
    @TableHeader(name = "Идентификатор опрошенного")
    @PrimaryKeyColumn(name = "respondent_id", type = PrimaryKeyType.PARTITIONED)
    private UUID respondentId;

    @TableHeader(name = "Уровень стресса")
    @Column("stress_level")
    private Integer stressLevel;

    @TableHeader(name = "Уровень тревоги")
    @Column("anxiety_level")
    private Integer anxietyLevel;

    @TableHeader(name = "Уровень депрессии")
    @Column("depression_level")
    private Integer depressionLevel;

    @TableHeader(name = "Дата опроса")
    @IgnoreEdit
    @Column("survey_date")
    private LocalDate surveyDate = LocalDate.now(); 
}
