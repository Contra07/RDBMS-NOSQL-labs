package ru.ssau.lab2.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
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
public class MentalStats 
{
    @Id
    @IgnoreEdit
    @TableHeader(name = "Идентификатор")
    private Integer id;
    @TableHeader(name = "Идентификатор опрошенного")
    private Integer userId;
    @TableHeader(name = "Уровень стресса")
    private Integer stressLevel;
    @TableHeader(name = "Уровень тревоги")
    private Integer anxietyLevel;
    @TableHeader(name = "Уровень депрессии")
    private Integer depressionLevel;
    @IgnoreEdit
    @TableHeader(name = "Дата опроса")
    private LocalDate date = LocalDate.now();
}
