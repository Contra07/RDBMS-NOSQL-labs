package ru.ssau.lab2.model;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.IgnoreEdit;
import ru.ssau.shared.shell.TableHeader;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class DemographicsCollapsing
{
    @IgnoreEdit
    @TableHeader(name = "Идентификатор")
    protected Integer id;
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
    @IgnoreEdit
    @TableHeader(name = "Знак")
    private Integer sign;
}