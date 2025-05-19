package ru.ssau.lab2.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab2.model.DemographicsReplacing;
import ru.ssau.lab2.repository.DemographicsReplacingRepository;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = {"people-replacing"}, group = "CRUD люди Replacing")
@RequiredArgsConstructor
public class DemographicsReplacingCommand
{
    protected static final String NULL = "NULL";
    protected final Terminal terminal;
    protected final ComponentFlow.Builder componentFlowBuilder;
    private final DemographicsReplacingRepository repository;

    @Command(command = {"list"}, description = "Вывести список")
    public void list(
        @Option(longNames = "replacing", shortNames = 'r') Boolean replacing,
        @Option(longNames = "final", shortNames = 'f') Boolean isFinal
    )
    {
        if(replacing)
        {
            var items = repository.findAllReplacing();
            terminal.writer().println(ShellUtils.createTable(items, new DemographicsReplacing(), 0).render(0));
        }
        else if(isFinal)
        {
            var items = repository.findAllFinal();
            terminal.writer().println(ShellUtils.createTable(items, new DemographicsReplacing(), 0).render(0));
        }
        else
        {
            var items = repository.findAll();
            terminal.writer().println(ShellUtils.createTable(items, new DemographicsReplacing(), 0).render(0));
        }
    }

    @Command(command = {"show"}, description = "Вывести")
    public void show(
        @Option(longNames = "id", shortNames = 'i') Integer id,
        @Option(longNames = "replacing", shortNames = 'r') Boolean replacing,
        @Option(longNames = "final", shortNames = 'f') Boolean isFinal
    )
    {
        if(replacing)
        {
            var items = repository.findByIdReplacing(id);
            terminal.writer().println(ShellUtils.createTable(items, new DemographicsReplacing(), 0).render(0));
        } 
        else if (isFinal)
        {
            var items = repository.findByIdFinal(id);
            terminal.writer().println(ShellUtils.createTable(items, new DemographicsReplacing(), 0).render(0));
        }
        else
        {
            var items = repository.findById(id);
            terminal.writer().println(ShellUtils.createTable(items, new DemographicsReplacing(), 0).render(0));
        }
    }

    @Command(command = {"new"}, description = "Создать")
    public void create()
    {
        var item = ShellUtils.askForItem(componentFlowBuilder, new DemographicsReplacing());
        repository.insert(item);
    }

    @Command(command = {"delete"}, description = "Удалить")
    public void delete(
        @Option(longNames = "id", shortNames = 'i') Integer id,
        @Option(longNames = "light", shortNames = 'l') boolean lightweight,
        @Option(longNames = "replacing", shortNames = 'r') Boolean replacing
    ) throws Exception
    {
        if (id != null) 
        {
            if(lightweight)
            {
                repository.delete(id);
            }
            else if (replacing)
            {
                repository.deleteReplacing(id);
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
        @Option(longNames = "replacing", shortNames = 'r') Boolean replacing
    ) throws Exception
    {
        if (id != null) 
        {
            var items = repository.findByIdFinal(id);
            if(items.size() > 0)
            {
                if(replacing)
                {
                    var item = ShellUtils.askForItem(componentFlowBuilder, items.getFirst());
                    repository.updateReplacing(id, item);
                }
                else
                {
                    repository.updateAlter(id, ShellUtils.askForItem(componentFlowBuilder, items.getFirst()));
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

