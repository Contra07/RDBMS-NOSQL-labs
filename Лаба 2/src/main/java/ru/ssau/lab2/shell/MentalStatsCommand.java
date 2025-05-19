package ru.ssau.lab2.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab2.model.AvgMentalStatsPerUser;
import ru.ssau.lab2.model.AvgMentalStatsPerYears;
import ru.ssau.lab2.model.MentalStats;
import ru.ssau.lab2.repository.DemographicsRepository;
import ru.ssau.lab2.repository.MentalStatsRepository;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = {"stats"}, group = "CRUD ментальный опрос")
@RequiredArgsConstructor
public class MentalStatsCommand
{
    protected static final String NULL = "NULL";
    protected final Terminal terminal;
    protected final ComponentFlow.Builder componentFlowBuilder;
    private final MentalStatsRepository repository;
    private final DemographicsRepository demographicsRepository;

    @Command(command = {"show"}, description = "Вывести")
    public void show(
        @Option(longNames = "id", shortNames = 'i') Integer id
    )
    {
        var items = repository.findById(id);
        terminal.writer().println(ShellUtils.createTable(items, new MentalStats(), 0).render(0));
    }

    @Command(command = {"new"}, description = "Создать")
    public void create() throws Exception
    {
        var item = ShellUtils.askForItem(componentFlowBuilder, new MentalStats());
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
        terminal.writer().println(ShellUtils.createTable(items, new MentalStats(), 0).render(0));
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
    
    @Command(command = {"average"}, description = "Среднее")
    public void average()
    {
        var items = repository.findAveragePerUser();
        terminal.writer().println();
        terminal.writer().println("Средние значения ментальной статистики для каждого опрошенного");
        terminal.writer().println();
        terminal.writer().println(ShellUtils.createTable(items, new AvgMentalStatsPerUser(), 0).render(0));
    }

    @Command(command = {"compare"}, description = "Сравнить")
    public void compare(
        @Option(longNames = "id", shortNames = 'i') Integer id,
        @Option(longNames = "first_year", shortNames = 'f') Integer firstYear,
        @Option(longNames = "second_year", shortNames = 's') Integer secondYear
    )
    {
        var items = repository.findAveragePerYear(id, firstYear, secondYear);
        terminal.writer().println();
        terminal.writer().println("Сравнение значения ментальной статистики для " + id + " опрошенного за " + firstYear + " и " + secondYear + " год");
        terminal.writer().println();
        terminal.writer().println(ShellUtils.createTable(items, new AvgMentalStatsPerYears(), 0).render(0));
    }
}
