package ru.ssau.lab2.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab2.model.DemographicsCollapsing;
import ru.ssau.lab2.repository.DemographicsCollapsingRepository;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = {"people-collapsing"}, group = "CRUD люди Collapsing")
@RequiredArgsConstructor
public class DemographicsCollapsingCommand
{
    protected static final String NULL = "NULL";
    protected final Terminal terminal;
    protected final ComponentFlow.Builder componentFlowBuilder;
    private final DemographicsCollapsingRepository repository;

    @Command(command = {"list"}, description = "Вывести список")
    public void list(
        @Option(longNames = "collapsing", shortNames = 'c') Boolean collapsing,
        @Option(longNames = "final", shortNames = 'f') Boolean isFinal
    )
    {
        if(collapsing)
        {
            var items = repository.findAllIdsCollapsing();
            terminal.writer().println();
            terminal.writer().println("Идентификатор");
            for (Integer integer : items) 
            {
                terminal.writer().println(integer);
            }
        }
        else if(isFinal)
        {
            var items = repository.findAllFinal();
            terminal.writer().println(ShellUtils.createTable(items, new DemographicsCollapsing(), 0).render(0));
        }
        else
        {
            var items = repository.findAll();
            terminal.writer().println(ShellUtils.createTable(items, new DemographicsCollapsing(), 0).render(0));
        }
    }

    @Command(command = {"show"}, description = "Вывести")
    public void show(
        @Option(longNames = "id", shortNames = 'i') Integer id,
        @Option(longNames = "collapsing", shortNames = 'c') Boolean collapsing,
        @Option(longNames = "final", shortNames = 'f') Boolean isFinal
    )
    {
        if(collapsing)
        {
            var items = repository.findIdsByIdCollapsing(id);
            terminal.writer().println();
            terminal.writer().println("Идентификатор");
            for (Integer integer : items) 
            {
                terminal.writer().println(integer);
            }
        } 
        else if (isFinal)
        {
            var items = repository.findByIdFinal(id);
            terminal.writer().println(ShellUtils.createTable(items, new DemographicsCollapsing(), 0).render(0));
        }
        else
        {
            var items = repository.findById(id);
            terminal.writer().println(ShellUtils.createTable(items, new DemographicsCollapsing(), 0).render(0));
        }
    }

    @Command(command = {"new"}, description = "Создать")
    public void create()
    {
        var item = ShellUtils.askForItem(componentFlowBuilder, new DemographicsCollapsing());
        repository.insert(item);
    }

    @Command(command = {"delete"}, description = "Удалить")
    public void delete(
        @Option(longNames = "id", shortNames = 'i') Integer id,
        @Option(longNames = "light", shortNames = 'l') boolean lightweight,
        @Option(longNames = "collapsing", shortNames = 'c') Boolean collapsing
    ) throws Exception
    {
        if (id != null) 
        {
            if(lightweight)
            {
                repository.delete(id);
            }
            else if (collapsing)
            {
                repository.deleteCollapsing(id);
            }
            else
            {
                repository.deleteAlter(id);
            }
        }
        else
        {
            throw new Exception("Не указан идентификатор");
        }
    }

    @Command(command = {"edit"}, description = "Изменить")
    public void edit(
        @Option(longNames = "id", shortNames = 'i', required = true) Integer id,
        @Option(longNames = "collapsing", shortNames = 'c') Boolean collapsing
    ) throws Exception
    {
        if (id != null) 
        {
            var items = repository.findByIdFinal(id);
            if(items.size() > 0)
            {
                if(collapsing)
                {
                    var item = ShellUtils.askForItem(componentFlowBuilder, items.getFirst());
                    repository.deleteCollapsing(id);
                    repository.insert(item);
                }
                else
                {
                    var item = ShellUtils.askForItem(componentFlowBuilder, items.getFirst());
                    repository.updateAlter(id, item);
                }
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

    @Command(command = {"optimize"}, description = "Запустить все merge")
    public void optimize()
    {
        repository.optimize();
    }
}
