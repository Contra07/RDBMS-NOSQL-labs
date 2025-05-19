package ru.ssau.lab5.model;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.ssau.shared.shell.IgnoreEdit;
import ru.ssau.shared.shell.TableHeader;

@Data
@EqualsAndHashCode(callSuper=false)
public class PersonWithCity extends Person
{
    @TableHeader(name = "Город")
    private String cityName;
    @IgnoreEdit
    @TableHeader(name = "Дата")
    private LocalDate date;

    public PersonWithCity() {
        super();
    }
    public PersonWithCity(String id, String name, Integer age, Gender gender, String occupation, String cityName,
            LocalDate date) {
        super(id, name, age, gender, occupation);
        this.cityName = cityName;
        this.date = date;
    }
}
