package ru.ssau.lab1new.pojo;

import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.ssau.lab1new.model.FileAccessType;
import ru.ssau.lab1new.model.ProcessFile;
import ru.ssau.lab1new.view.custom.TableHeader;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Component
@Primary
@Scope("prototype")
public class ProcessFilePojo extends IdPojo
{
    @TableHeader(name = "Файл")
    private UUID item;
    @TableHeader(name = "Процесс")
    private UUID process;
    @TableHeader(name = "Тип доступа")
    private FileAccessType access;

    public ProcessFilePojo(UUID id, UUID item, UUID process, FileAccessType access) {
        super(id);
        this.item = item;
        this.process = process;
        this.access = access;
    }

    public static ProcessFilePojo fromEntity(ProcessFile entity)
    {
        return new ProcessFilePojo(entity.getId(), entity.getItemId(), entity.getProcessId(), entity.getAccess());
    }

    public static ProcessFile toEntity(ProcessFilePojo pojo)
    {
        var processFile = new ProcessFile(pojo.getId(), pojo.getProcess(),pojo.getItem(), pojo.getAccess());
        return processFile;
    }

    @Override
    public IdPojo clone() 
    {
        return new ProcessFilePojo(id, item, process, access);
    }
}
