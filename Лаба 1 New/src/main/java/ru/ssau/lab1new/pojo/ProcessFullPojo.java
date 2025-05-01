package ru.ssau.lab1new.pojo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.ssau.lab1new.projection.ProcessFullPathProjection;
import ru.ssau.lab1new.view.custom.TableHeader;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Component
@Scope("prototype")
public class ProcessFullPojo extends ProcessPojo
{
    @TableHeader(name = "Время работы")
    private Duration operatingTime;

    @TableHeader(name = "Полный путь до файла")
    private String fullPath;

    private int depth;

    public ProcessFullPojo(UUID id, String name, UUID parent, List<String> arguments, UUID sourceFile,
    LocalDateTime startedAt, Duration operatingTime, String fullPath, int depth) 
    {
        super(id, name, parent, arguments, sourceFile, startedAt);
        this.operatingTime = operatingTime;
        this.fullPath = fullPath;
        this.depth = depth;
    }

    public static ProcessFullPojo fromProjection(ProcessFullPathProjection projection)
    {
         return new ProcessFullPojo(
            projection.getId(), 
            projection.getName(), 
            projection.getParentId(), 
            projection.getArguments(), 
            projection.getSourceFile(), 
            projection.getStartedAt(), 
            Duration.between(projection.getStartedAt(), LocalDateTime.now()), 
            String.join("/", projection.getFullPath()), 
            projection.getPath().length
        );
    }
    
}
