package ru.ssau.lab1new.pojo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.lab1new.projection.ItemCompareProjection;
import ru.ssau.lab1new.view.custom.TableHeader;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Scope("prototype")
public class ItemComparePojo 
{
    @TableHeader(name = "Файлы в первой папке")
    private String firstFile;
    @TableHeader(name = "Файлы во второй папке")
    private String secondFile;

    public static ItemComparePojo fromProjection(ItemCompareProjection projection)
    {
        return new ItemComparePojo(projection.getFirstName(), projection.getSecondName());
    }
}
