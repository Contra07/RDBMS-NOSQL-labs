package ru.ssau.lab4.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.IgnoreEdit;
import ru.ssau.shared.shell.TableHeader;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Primary
@Component
public class MentalStats implements Entitiable<MentalStats>
{
    @IgnoreEdit
    @TableHeader(name = "Идентификатор")
    protected String id;
    @TableHeader(name = "Идентификатор опрошенного")
    private String userId;
    @TableHeader(name = "Уровень стресса")
    private Integer stressLevel;
    @TableHeader(name = "Уровень тревоги")
    private Integer anxietyLevel;
    @TableHeader(name = "Уровень депрессии")
    private Integer depressionLevel;
    @IgnoreEdit
    @TableHeader(name = "Дата опроса")
    private LocalDate date = LocalDate.now();

    @Override
    public MentalStats clone() 
    {
        var m = new MentalStats(id, userId, stressLevel, anxietyLevel, depressionLevel, date);
        return m;
    }

    @Override
    public MentalStats fromStringValues(Map<String, String> values) 
    {
        var mentalStats = new MentalStats();
        // Обработка поля id
        if (values.containsKey("id")) {
            try {
                String stringValue = values.get("id");
                if (stringValue != null && !stringValue.isEmpty()) {
                    mentalStats.id = stringValue;
                }
            } catch (NumberFormatException e) {
                System.err.println("Неверный формат id: " + values.get("id"));
            }
        }

        // Обработка поля userId
        if (values.containsKey("userId")) {
            try {
                String stringValue = values.get("userId");
                if (stringValue != null && !stringValue.isEmpty()) {
                    mentalStats.userId = stringValue;
                }
            } catch (NumberFormatException e) {
                System.err.println("Неверный формат userId: " + values.get("userId"));
            }
        }

        // Обработка поля stressLevel
        if (values.containsKey("stressLevel")) {
            try {
                String stringValue = values.get("stressLevel");
                if (stringValue != null && !stringValue.isEmpty()) {
                    mentalStats.stressLevel = Integer.parseInt(stringValue);
                }
            } catch (NumberFormatException e) {
                System.err.println("Неверный формат stressLevel: " + values.get("stressLevel"));
            }
        }

        // Обработка поля anxietyLevel
        if (values.containsKey("anxietyLevel")) {
            try {
                String stringValue = values.get("anxietyLevel");
                if (stringValue != null && !stringValue.isEmpty()) {
                    mentalStats.anxietyLevel = Integer.parseInt(stringValue);
                }
            } catch (NumberFormatException e) {
                System.err.println("Неверный формат anxietyLevel: " + values.get("anxietyLevel"));
            }
        }

        // Обработка поля depressionLevel
        if (values.containsKey("depressionLevel")) {
            try {
                String stringValue = values.get("depressionLevel");
                if (stringValue != null && !stringValue.isEmpty()) {
                    mentalStats.depressionLevel = Integer.parseInt(stringValue);
                }
            } catch (NumberFormatException e) {
                System.err.println("Неверный формат depressionLevel: " + values.get("depressionLevel"));
            }
        }

        // Обработка поля date
        if (values.containsKey("date")) {
            try {
                String stringValue = values.get("date");
                if (stringValue != null && !stringValue.isEmpty()) {
                    mentalStats.date = LocalDate.parse(stringValue);
                }
            } catch (DateTimeParseException e) {
                System.err.println("Неверный формат даты: " + values.get("date"));
            }
        }

        return mentalStats;
    }
}