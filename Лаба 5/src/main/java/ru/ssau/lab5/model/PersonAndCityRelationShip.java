package ru.ssau.lab5.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.IgnoreEdit;
import ru.ssau.shared.shell.TableHeader;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonAndCityRelationShip
{
    @TableHeader(name = "Имя опрошенного")
    private String personName;
    @TableHeader(name = "Город")
    private String cityName;
    @TableHeader(name = "Дата")
    @IgnoreEdit
    private LocalDate date;
}
