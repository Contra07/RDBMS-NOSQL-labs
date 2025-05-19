package ru.ssau.lab4.shell;

import org.jline.terminal.Terminal;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow.Builder;

import ru.ssau.lab4.model.Entitiable;
import ru.ssau.shared.shell.AbstractCommand;
import ru.ssau.shared.shell.ShellUtils;

public class CrudCommand<T extends Entitiable<T>> extends AbstractCommand
{
    protected final ListCrudRepository<T, String> repository;
    protected final T entity;

    public CrudCommand(Terminal terminal, Builder componentFlowBuilder, ListCrudRepository<T, String> repository,
    T entity) {
        super(terminal, componentFlowBuilder);
        this.repository = repository;
        this.entity = entity;
    }

    @Command(command = {"show"}, description = "Вывести")
    public void show(
        @Option(longNames = "id", shortNames = 'i') String id
    ) throws Exception
    {
        var item = repository.findById(id);
        if(item.isPresent())
            terminal.writer().println(ShellUtils.createItemFieldsTable(item.get()).render(0));
        else
            terminal.writer().println("Не удалось найти объект с идентификатором: " + id);
    }

    @Command(command = {"new"}, description = "Создать")
    public void create() throws Exception
    {
        var item = ShellUtils.askForItem(componentFlowBuilder, entity.clone());
        var newItem = repository.save(item);
        if(newItem != null)
            terminal.writer().println(ShellUtils.createItemFieldsTable(newItem).render(0));
        else
            terminal.writer().println("Не удалось получить созданный объект");
    }

    @Command(command = {"list"}, description = "Вывести список")
    public void list() throws Exception
    {
        var items = repository.findAll();
        terminal.writer().println(ShellUtils.createTable(items, (T)entity.clone(), 0).render(0));
    }

    @Command(command = {"delete"}, description = "Удалить")
    public void delete(
        @Option(longNames = "id", shortNames = 'i') String id
    ) throws Exception
    {
        if (id != null) 
        {
            repository.deleteById(id);
        }
        else
        {
            throw new Exception("Не указан идентификатор");
        }
    }

    @Command(command = {"edit"}, description = "Изменить")
    public void edit(
        @Option(longNames = "id", shortNames = 'i', required = true) String id
    ) throws Exception
    {
        if (id != null) 
        {
            var items = repository.findById(id);
            if(items.isPresent())
            {
                var newItem = repository.save(
                    ShellUtils.askForItem(componentFlowBuilder, items.get())
                );
                if(newItem != null)
                    terminal.writer().println(ShellUtils.createItemFieldsTable(newItem).render(0));
                else
                    terminal.writer().println("Не удалось получить созданный объект");
            }
            else
            {
                throw new Exception("Не наидена запись с идентификатором " + id);
            }
        }
        else
        {
            throw new Exception("Не указан идентификатор");
        }
    }
}
