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
public class AvgMentalStatsPerYears {
    @TableHeader(name = "Номер месяца")
    private Integer month;
    @TableHeader(name = "Средний уровень стресса в первом году")
    private Double firstStressLevel;
    @TableHeader(name = "Средний уровень депрессии в первом году")
    private Double firstDepressionLevel;
    @TableHeader(name = "Средний уровень тревоги в первом году")
    private Double firstAnxietyLevel;
    @TableHeader(name = "Средний уровень стресса во втором году")
    private Double secondStressLevel;
    @TableHeader(name = "Средний уровень депрессии во втором году")
    private Double secondDepressionLevel;
    @TableHeader(name = "Средний уровень тревоги во втором году")
    private Double secondAnxietyLevel;
}
