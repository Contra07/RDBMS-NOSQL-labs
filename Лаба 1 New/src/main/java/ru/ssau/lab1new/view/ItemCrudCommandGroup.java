package ru.ssau.lab1new.view;

import java.util.UUID;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.component.flow.ComponentFlow;

import ru.ssau.lab1new.pojo.ItemPojo;
import ru.ssau.lab1new.service.CRUDService;
import ru.ssau.lab1new.view.custom.CrudCommand;

@Command(command = {"item"}, group = "CRUD файлы")
public class ItemCrudCommandGroup extends CrudCommand<ItemPojo>
{
    public ItemCrudCommandGroup(Terminal terminal, ComponentFlow.Builder componentFlowBuilder, ItemPojo entity, CRUDService<ItemPojo, UUID> service) {
        super(terminal, componentFlowBuilder, entity, service);
    }
}
