package ru.ssau.lab3.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.IgnoreEdit;
import ru.ssau.shared.shell.TableHeader;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class IdEntity<T extends Number>
{
    @IgnoreEdit
    @TableHeader(name = "Идентификатор")
    protected T id;

    public abstract IdEntity<T> fromStringValues(Map<String, String> values);
    public abstract IdEntity<T> clone();
}
