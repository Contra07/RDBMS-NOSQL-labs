package ru.ssau.lab1new.pojo;

import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.ssau.lab1new.model.FileAccessType;
import ru.ssau.lab1new.projection.ProcessFileFullProjection;
import ru.ssau.lab1new.view.custom.TableHeader;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Component
@Scope("prototype")
public class ProcessFileFullPojo extends ProcessFilePojo
{
    @TableHeader(name = "Полный путь")
    private String fullPath;
    @TableHeader(name = "Имя процесса")
    private String processName;

    public ProcessFileFullPojo(UUID id, UUID item, UUID process, FileAccessType access, String fullPath, String processName) 
    {
        super(id, item, process, access);
        this.fullPath = fullPath;
        this.processName = processName;
    }

    public static ProcessFileFullPojo fromProjection(ProcessFileFullProjection projection)
    {
        return new ProcessFileFullPojo
        (
            projection.getId() == null ? null : projection.getId(), 
            projection.getItemId() == null ? null : projection.getItemId() , 
            projection.getProcessId(), 
            projection.getAccess() == null ? null : projection.getAccess(), 
            projection.getFullPath() == null ? null : String.join("/", projection.getFullPath()), 
            projection.getProcessName() == null ? null : projection.getProcessName()
        );
    }
}
