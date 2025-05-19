package ru.ssau.shared.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.shared.shell.IgnoreEdit;
import ru.ssau.shared.shell.TableHeader;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class IdPojo<I> implements CloneablePojo
{
    @IgnoreEdit
    @TableHeader(name = "Идентификатор")
    protected I id;
    @Override
    public abstract IdPojo<I> clone();
}
