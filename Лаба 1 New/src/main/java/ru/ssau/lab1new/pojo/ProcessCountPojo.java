package ru.ssau.lab1new.pojo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.lab1new.model.FileAccessType;
import ru.ssau.lab1new.projection.ProcessWithFileCountProjection;
import ru.ssau.lab1new.view.custom.TableHeader;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Scope("prototype")
public class ProcessCountPojo 
{
    @TableHeader(name = "Идентификатор процесса")
    private String processId;
    @TableHeader(name = "Имя процесса")
    private String processName;
    @TableHeader(name = "Открыто файлов")
    private int filesCount;
    @TableHeader(name = "Тип доступа")
    private FileAccessType accessType;

    public static ProcessCountPojo fromProjection(ProcessWithFileCountProjection projection)
    {
        return new ProcessCountPojo
        (
            projection.getProcessId() == null ? null : projection.getProcessId(), 
            projection.getProcessName() == null ? null : projection.getProcessName() , 
            projection.getFilesNumber(), 
            projection.getAccess() == null ? null :projection.getAccess()
        );
    }
}
