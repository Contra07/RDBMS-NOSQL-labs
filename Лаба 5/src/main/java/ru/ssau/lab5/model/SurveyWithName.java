package ru.ssau.lab5.model;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.ssau.shared.shell.IgnoreEdit;
import ru.ssau.shared.shell.TableHeader;

@Data
@EqualsAndHashCode(callSuper=false)
public class SurveyWithName extends Survey
{
    @TableHeader(name = "Имя")
    private String name;
    @IgnoreEdit
    @TableHeader(name = "Дата")
    private LocalDateTime date;
    public SurveyWithName() {
        super();
    }
    public SurveyWithName(String _id, Integer id, Integer stressLevel, Integer anxietyLevel, Integer depressionLevel,
            String cityName, LocalDateTime date) {
        super(_id, id, stressLevel, anxietyLevel, depressionLevel);
        this.name = cityName;
        this.date = date;
    }
    
}
