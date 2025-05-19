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
public class AvgMentalStatsPerUser 
{
    @TableHeader(name = "Идентификатор")
    private Integer id;
    @TableHeader(name = "Занятость")
    private String occupation;
    @TableHeader(name = "Место жительства")
    private String location;
    @TableHeader(name = "Средний уровень стресса")
    private Integer stressLevel;
    @TableHeader(name = "Средний уровень тревоги")
    private Integer anxietyLevel;
    @TableHeader(name = "Средний уровень депрессии")
    private Integer depressionLevel;
}
