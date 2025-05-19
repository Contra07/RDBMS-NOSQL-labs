package ru.ssau.lab2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.TableHeader;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OccupationAndSleep 
{
    @TableHeader(name = "Занятость")
    String occupation;
    @TableHeader(name = "Среднее количество часов сна в сутки")
    Double avgSleepHours;
}
