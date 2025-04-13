package ru.ssau.lab1.pojo;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.lab1.view.custom.ColumnName;
import ru.ssau.lab1.view.custom.EditTransient;
import ru.ssau.lab1.model.Item;
import ru.ssau.lab1.model.Process;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Scope("prototype")
public class ProcessPojo
{
    @EditTransient
    private UUID id;
    @ColumnName(name = "Имя")
    private String name;
    @ColumnName(name = "Родительский процесс")
    private UUID parent;
    @ColumnName(name = "Аргументы")
    private List<String> arguments;
    @ColumnName(name = "Файл")
    private UUID sourceFile;

    public static ProcessPojo fromEntity(Process entity)
    {
        
        return new ProcessPojo(entity.getId(), entity.getName(), entity.getParentId(), entity.getArguments(), entity.getSourceFileId());
    }

    public static Process toEntity(ProcessPojo pojo)
    {
        var process = new Process(pojo.getId(), pojo.getName(), null, pojo.getParent(), pojo.getArguments(), null, pojo.getSourceFile());
        if(pojo.getParent() != null)
        {
            var parent = new Process();
            parent.setId(pojo.getParent());
            process.setParent(parent);
        }
        if(pojo.getSourceFile() != null)
        {
            var sourceFile = new Item();
            sourceFile.setId(pojo.getSourceFile());
            process.setSourceFile(sourceFile);
        }
        return process;
    }

}