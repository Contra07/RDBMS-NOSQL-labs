package ru.ssau.lab3.model;

import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.TableHeader;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Primary
@Component
public class Habits extends IdEntity<Integer>
{
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

    @Override
    public Habits clone() 
    {
        var h = new Habits(userId, sleepHours, physicalActivityDays, socialInteractionFreq, year);
        h.setId(id);
        return h;
    }

    @Override
    public Habits fromStringValues(Map<String, String> values)
    {
        var habits = new Habits();
        // Обработка поля id
        if (values.containsKey("id")) {
            try {
                String stringValue = values.get("id");
                if (stringValue != null && !stringValue.isEmpty()) {
                    habits.id = Integer.parseInt(stringValue);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid id format: " + values.get("id"));
            }
        }

        // Обработка поля userId
        if (values.containsKey("userId")) {
            try {
                String stringValue = values.get("userId");
                if (stringValue != null && !stringValue.isEmpty()) {
                    habits.userId = Integer.parseInt(stringValue);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid userId format: " + values.get("userId"));
            }
        }

        // Обработка поля sleepHours
        if (values.containsKey("sleepHours")) {
            try {
                String stringValue = values.get("sleepHours");
                if (stringValue != null && !stringValue.isEmpty()) {
                    habits.sleepHours = Double.parseDouble(stringValue);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid sleepHours format: " + values.get("sleepHours"));
            }
        }

        // Обработка поля physicalActivityDays
        if (values.containsKey("physicalActivityDays")) {
            try {
                String stringValue = values.get("physicalActivityDays");
                if (stringValue != null && !stringValue.isEmpty()) {
                    habits.physicalActivityDays = Integer.parseInt(stringValue);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid physicalActivityDays format: " + values.get("physicalActivityDays"));
            }
        }

        // Обработка поля socialInteractionFreq
        if (values.containsKey("socialInteractionFreq")) {
            try {
                String stringValue = values.get("socialInteractionFreq");
                if (stringValue != null && !stringValue.isEmpty()) {
                    habits.socialInteractionFreq = ActivityFrequency.valueOf(stringValue);
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid socialInteractionFreq value: " + values.get("socialInteractionFreq"));
            }
        }

        // Обработка поля year
        if (values.containsKey("year")) {
            try {
                String stringValue = values.get("year");
                if (stringValue != null && !stringValue.isEmpty()) {
                    habits.year = Integer.parseInt(stringValue);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid year format: " + values.get("year"));
            }
        }

        return habits;
    }
}
