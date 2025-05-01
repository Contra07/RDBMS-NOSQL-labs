package ru.ssau.lab1new.pojo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.ssau.lab1new.model.Process;
import ru.ssau.lab1new.projection.ProcessProjection;
import ru.ssau.lab1new.view.custom.TableHeader;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Component
@Primary
@Scope("prototype")
public class ProcessPojo extends IdPojo
{
    @TableHeader(name = "Имя")
    private String name;
    @TableHeader(name = "Родительский процесс")
    private UUID parent;
    @TableHeader(name = "Аргументы")
    private List<String> arguments;
    @TableHeader(name = "Дата запуска")
    private LocalDateTime startedAt;
    @TableHeader(name = "Исполняемый файл")
    private UUID sourceFile;

    public ProcessPojo(UUID id, String name, UUID parent, List<String> arguments, UUID sourceFile, LocalDateTime startedAt) {
        super(id);
        this.name = name;
        this.parent = parent;
        this.arguments = arguments;
        this.sourceFile = sourceFile;
        this.startedAt = startedAt;
    }

    public static ProcessPojo fromEntity(Process entity)
    {
        return new ProcessPojo(entity.getId(), entity.getName(), entity.getParentId(), entity.getArguments(), entity.getSourceFileId(), entity.getStartedAt());
    }

    public static ProcessPojo fromProjection(ProcessProjection projection)
    {
        return new ProcessPojo(projection.getId(), projection.getName(), projection.getParentId(), projection.getArguments(), projection.getSourceFile(), projection.getStartedAt());
    }

    public static Process toEntity(ProcessPojo pojo)
    {
        var process = new Process(pojo.getId(), pojo.getName(), pojo.getParent(), pojo.getArguments(), pojo.getSourceFile(), pojo.getStartedAt());
        return process;
    }

    @Override
    public ProcessPojo clone() 
    {
        return new ProcessPojo(id, name, parent, arguments, sourceFile, startedAt);
    }

}