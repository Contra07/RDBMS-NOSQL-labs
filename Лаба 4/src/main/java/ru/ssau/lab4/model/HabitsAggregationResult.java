package ru.ssau.lab4.model;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.TableHeader;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitsAggregationResult 
{
    @TableHeader(name = "Идентификатор опрошенного")
    @Field("_id")
    private String userId;
    @TableHeader(name = "Среднее количество часов сна")
    private Double avgSleepHours;
    @TableHeader(name = "Среднее количество дней физической активности")
    private Double avgPhysicalActivityDays;
}