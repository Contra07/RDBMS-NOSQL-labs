package ru.ssau.lab3.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.IgnoreEdit;
import ru.ssau.shared.shell.TableHeader;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Primary
@Component
public class Demographics extends IdEntity<Integer>
{
    @TableHeader(name = "Возраст")
    private Integer age;
    @TableHeader(name = "Пол")
    private Gender gender;
    @TableHeader(name = "Занятость")
    private String occupation;
    @TableHeader(name = "Место жительства")
    private String location;
    @IgnoreEdit
    @TableHeader(name = "Дата регистрации")
    private LocalDate registrationDate = LocalDate.now();

    @Override
    public Demographics clone() 
    {
        var d =new Demographics(
            age,
            gender,
            occupation,
            location,
            registrationDate
        );
        d.setId(id);
        return d;
    }

    @Override
    public Demographics fromStringValues(Map<String, String> values) 
    {
        var demographics = new Demographics();
        // Обработка поля id
        if (values.containsKey("id")) 
        {
            try 
            {
                String stringValue = values.get("id");
                if (stringValue != null && !stringValue.isEmpty()) {
                    demographics.setId(Integer.parseInt(stringValue));
                }
            } 
            catch (NumberFormatException e) 
            {
                System.err.println("Invalid id format: " + values.get("id"));
            }
        }

        // Обработка поля age
        if (values.containsKey("age")) {
            try {
                String stringValue = values.get("age");
                if (stringValue != null && !stringValue.isEmpty()) {
                    demographics.setAge(Integer.parseInt(stringValue));
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid age format: " + values.get("age"));
            }
        }

        // Обработка поля gender
        if (values.containsKey("gender")) {
            try {
                String stringValue = values.get("gender");
                if (stringValue != null && !stringValue.isEmpty()) {
                    demographics.setGender(Gender.valueOf(stringValue));
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid gender value: " + values.get("gender"));
            }
        }

        // Обработка поля occupation
        if (values.containsKey("occupation")) {
            demographics.occupation = values.get("occupation");
        }

        // Обработка поля location
        if (values.containsKey("location")) {
            demographics.location = values.get("location");
        }

        // Обработка поля registrationDate
        if (values.containsKey("registrationDate")) {
            try {
                String stringValue = values.get("registrationDate");
                if (stringValue != null && !stringValue.isEmpty()) {
                    demographics.registrationDate = LocalDate.parse(stringValue);
                }
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format: " + values.get("registrationDate"));
            }
        }

        return demographics;
    }
}
