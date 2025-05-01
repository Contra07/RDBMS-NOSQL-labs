package ru.ssau.lab1new.view;

import java.util.UUID;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.component.flow.ComponentFlow;

import ru.ssau.lab1new.pojo.ProcessPojo;
import ru.ssau.lab1new.service.CRUDService;
import ru.ssau.lab1new.view.custom.CrudCommand;

@Command(command = {"process"}, group = "CRUD процесс")
public class ProcessCrudCommandGroup extends CrudCommand<ProcessPojo>
{
    public ProcessCrudCommandGroup(Terminal terminal, ComponentFlow.Builder componentFlowBuilder, ProcessPojo entity, CRUDService<ProcessPojo, UUID> service) {
        super(terminal, componentFlowBuilder, entity, service);
    }
}
