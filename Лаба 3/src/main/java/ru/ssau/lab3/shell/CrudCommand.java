package ru.ssau.lab3.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab3.model.IdEntity;
import ru.ssau.lab3.model.KeyValuePair;
import ru.ssau.lab3.repository.crud.CrudRepository;
import ru.ssau.shared.shell.ShellUtils;

@RequiredArgsConstructor
public class CrudCommand<T extends IdEntity<Integer>>
{
    protected static final String NULL = "NULL";
    protected final Terminal terminal;
    protected final ComponentFlow.Builder componentFlowBuilder;
    protected final CrudRepository<T> repository;
    protected final T entity;

    @Command(command = {"show"}, description = "Вывести")
    public void show(
        @Option(longNames = "id", shortNames = 'i') Integer id
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
        var item = ShellUtils.askForItem(componentFlowBuilder, (T)entity.clone());
        var newItem = repository.create(item);
        if(newItem.isPresent())
            terminal.writer().println(ShellUtils.createItemFieldsTable(newItem.get()).render(0));
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
        @Option(longNames = "id", shortNames = 'i') Integer id
    ) throws Exception
    {
        if (id != null) 
        {
            var deleted = repository.deleteById(id);
            terminal.writer().println("Удалено ключей: " + deleted);
        }
        else
        {
            throw new Exception("Не указан идентификатор");
        }
    }

    @Command(command = {"edit"}, description = "Изменить")
    public void edit(
        @Option(longNames = "id", shortNames = 'i', required = true) Integer id
    ) throws Exception
    {
        if (id != null) 
        {
            var items = repository.findById(id);
            if(items.isPresent())
            {
                var newItem = repository.edit(
                    id, 
                    ShellUtils.askForItem(componentFlowBuilder, items.get())
                );
                if(newItem.isPresent())
                    terminal.writer().println(ShellUtils.createItemFieldsTable(newItem.get()).render(0));
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

    @Command(command = {"keys"}, description = "Значения по ключам")
    public void keys() throws Exception
    {
        var kvps = repository.findKeys();
        terminal.writer().println(ShellUtils.createTable(kvps, new KeyValuePair(), 0).render(0));
    }
}
