package ru.ssau.lab1new.view;

import java.util.UUID;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.component.flow.ComponentFlow.Builder;

import ru.ssau.lab1new.pojo.ProcessFilePojo;
import ru.ssau.lab1new.service.CRUDService;
import ru.ssau.lab1new.view.custom.CrudCommand;

@Command(command = {"process-file"}, group = "CRUD файлы процесса")
public class ProcessFileCrudCommandGroup extends CrudCommand<ProcessFilePojo>
{
    public ProcessFileCrudCommandGroup(Terminal terminal, Builder componentFlowBuilder, ProcessFilePojo entity, CRUDService<ProcessFilePojo, UUID> service) 
    {
        super(terminal, componentFlowBuilder, entity, service);
    }
}
