package ru.ssau.lab2.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab2.model.AvgHabitsPerYear;
import ru.ssau.lab2.model.Habits;
import ru.ssau.lab2.model.OccupationAndSleep;
import ru.ssau.lab2.model.OccupationAndSleepPerYear;
import ru.ssau.lab2.repository.DemographicsRepository;
import ru.ssau.lab2.repository.HabitsRepository;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = {"habits"}, group = "CRUD характеристика")
@RequiredArgsConstructor
public class HabitsCommand
{
    protected static final String NULL = "NULL";
    protected final Terminal terminal;
    protected final ComponentFlow.Builder componentFlowBuilder;
    private final HabitsRepository repository;
    private final DemographicsRepository demographicsRepository;

    @Command(command = {"show"}, description = "Вывести")
    public void show(
        @Option(longNames = "id", shortNames = 'i') Integer id
    )
    {
        var items = repository.findById(id);
        terminal.writer().println(ShellUtils.createTable(items, new Habits(), 0).render(0));
    }

    @Command(command = {"new"}, description = "Создать")
    public void create() throws Exception
    {
        var item = ShellUtils.askForItem(componentFlowBuilder, new Habits());
        if(demographicsRepository.findById(item.getUserId()).size() > 0)
        {
            repository.insert(item);
        }
        else
        {
            throw new Exception("Не удалось найти опрошенного с идентификатором " + item.getUserId());
        }
    }

    @Command(command = {"list"}, description = "Вывести список")
    public void list()
    {
        var items = repository.findAll();
        terminal.writer().println(ShellUtils.createTable(items, new Habits(), 0).render(0));
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
                repository.delete(id);
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
        @Option(longNames = "id", shortNames = 'i', required = true) Integer id
    ) throws Exception
    {
        if (id != null) 
        {
            var items = repository.findById(id);
            if(items.size() > 0)
            {
                var item = ShellUtils.askForItem(componentFlowBuilder, items.getFirst());
                if(demographicsRepository.findById(item.getUserId()).size() > 0)
                {
                    repository.updateAlter(id, item);
                }
                else
                {
                    throw new Exception("Не удалось найти опрошенного с идентификатором " + item.getUserId());
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

    @Command(command = {"sleep"}, description = "Средний сон")
    public void sleep(
        @Option(longNames = "year", shortNames = 'y', required = false) Integer year
    ) throws Exception
    {
        if(year != null && year != 0)
        {
            var items = repository.findOccupationAndSleepByYear(year);
            terminal.writer().println("");
            terminal.writer().println("Среднее количество сна по занятости в " + year + " году");
            terminal.writer().println("");
            terminal.writer().println(ShellUtils.createTable(items, new OccupationAndSleep(), 0).render(0));
        }
        else
        {
            var items = repository.findOccupationAndSleep();
            terminal.writer().println("");
            terminal.writer().println("Среднее количество сна по годам и занятости");
            terminal.writer().println("");
            terminal.writer().println(ShellUtils.createTable(items, new OccupationAndSleepPerYear(), 0).render(0));
        }
    }

    @Command(command = {"average"}, description = "Средннее по годам")
    public void average(
        @Option(longNames = "id", shortNames = 'i') Integer id
    )
    {
        var items = repository.findAvgHabitsByUser(id);
        terminal.writer().println("");
        terminal.writer().println("Среднее количество сна и физической активности для опрошенного " + id + " по годам");
        terminal.writer().println("");
        terminal.writer().println(ShellUtils.createTable(items, new AvgHabitsPerYear(), 0).render(0));
    }
}
