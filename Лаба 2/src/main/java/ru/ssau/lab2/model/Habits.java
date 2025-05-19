package ru.ssau.lab2.model;

import org.springframework.data.annotation.Id;
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
public class Habits
{
    @Id
    @IgnoreEdit
    @TableHeader(name = "Идентификатор")
    private Integer id;
    @TableHeader(name = "Идентификатор опрошенного")
    private Integer userId;
    @TableHeader(name = "Среднее количество часов сна в сутки")
    private Double sleepHours;
    @TableHeader(name = "Среднее количество дней в неделю с физической активностью")
    private Integer physicalActivityDays;
    @TableHeader(name = "Частота социальных взаимодействий")
    private ActivityFrequency socialInteractionFreq;
    @TableHeader(name = "Год")
    private Integer year; 
}
