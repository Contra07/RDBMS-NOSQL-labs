package ru.ssau.lab1new.pojo;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.lab1new.view.custom.IgnoreEdit;
import ru.ssau.lab1new.view.custom.TableHeader;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class IdPojo 
{
    @IgnoreEdit
    @TableHeader(name = "Идентификатор")
    protected UUID id;
    public abstract IdPojo clone();
}
