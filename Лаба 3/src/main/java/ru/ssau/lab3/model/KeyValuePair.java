package ru.ssau.lab3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.TableHeader;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValuePair 
{
    @TableHeader(name = "Ключ")
    private Object key;
    @TableHeader(name = "Значение")
    private Object value;
}