package ru.ssau.lab2.model;

import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.TableHeader;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AvgHabitsPerYear 
{
    @TableHeader(name = "Год")
    private Integer year;
    @TableHeader(name = "Среднее количество часов сна в сутки")
    private Double avgSleepHours;
    @TableHeader(name = "Среднее количество дней в неделю с физической активностью")
    private Double avgPhysicalActivityDays;
}