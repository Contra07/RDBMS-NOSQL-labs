package ru.ssau.lab5.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.TableHeader;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityNameAndCount 
{
    @TableHeader(name = "Количество опрошенных")
    private int personCount;
    @TableHeader(name = "Город")
    private String cityName;
}
