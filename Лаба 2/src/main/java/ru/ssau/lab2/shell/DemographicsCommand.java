package ru.ssau.lab2.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab2.model.Demographics;
import ru.ssau.lab2.repository.DemographicsRepository;
import ru.ssau.lab2.repository.HabitsRepository;
import ru.ssau.lab2.repository.MentalStatsRepository;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = {"people"}, group = "CRUD люди")
@RequiredArgsConstructor
public class DemographicsCommand
{
    protected static final String NULL = "NULL";
    protected final Terminal terminal;
    protected final ComponentFlow.Builder componentFlowBuilder;
    private final DemographicsRepository repository;
    private final HabitsRepository habitsRepository;
    private final MentalStatsRepository mentalStatsRepository;

    @Command(command = {"show"}, description = "Вывести")
    public void show(
        @Option(longNames = "id", shortNames = 'i') Integer id
    )
    {
        var items = repository.findById(id);
        terminal.writer().println(ShellUtils.createTable(items, new Demographics(), 0).render(0));
    }

    @Command(command = {"new"}, description = "Создать")
    public void create()
    {
        var item = ShellUtils.askForItem(componentFlowBuilder, new Demographics());
        repository.insert(item);
    }

    @Command(command = {"list"}, description = "Вывести список")
    public void list()
    {
        var items = repository.findAll();
        terminal.writer().println(ShellUtils.createTable(items, new Demographics(), 0).render(0));
    }

    @Command(command = {"delete"}, description = "Удалить")
    public void delete(
        @Option(longNames = "id", shortNames = 'i') Integer id,
        @Option(longNames = "light", shortNames = 'l') boolean lightweight
    ) throws Exception
    {
        if (id != null) 
        {
            if(lightweight)
            {
                habitsRepository.deleteByUser(id);
                mentalStatsRepository.deleteByUser(id);
                repository.delete(id);
            }
            else
            {
                habitsRepository.deleteAlterByUser(id);
                mentalStatsRepository.deleteAlterByUser(id);
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
        @Option(longNames = "id", shortNames = 'i', required = true) Integer id
    ) throws Exception
    {
        if (id != null) 
        {
            var items = repository.findById(id);
            if(items.size() > 0)
            {
                repository.updateAlter(id, ShellUtils.askForItem(componentFlowBuilder, items.getFirst()));
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
