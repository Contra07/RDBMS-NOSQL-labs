package ru.ssau.lab5.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.ssau.shared.shell.TableHeader;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CityNameAndAnxietyLevel 
{
    @TableHeader(name = "Средний уровень тревоги")
    private Double anxietyLevel;
    @TableHeader(name = "Город")
    private String cityName;
}
