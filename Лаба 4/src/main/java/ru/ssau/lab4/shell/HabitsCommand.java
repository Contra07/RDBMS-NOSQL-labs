package ru.ssau.lab4.shell;

import org.jline.terminal.Terminal;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow.Builder;

import ru.ssau.lab4.model.Habits;
import ru.ssau.lab4.model.HabitsAggregationResult;
import ru.ssau.lab4.repository.DemographicsRepository;
import ru.ssau.lab4.repository.HabitsRepository;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = {"habits"}, group = "CRUD характеристика")
public class HabitsCommand extends CrudCommand<Habits>
{
    private final DemographicsRepository demographicsRepository;
    private final HabitsRepository habitsRepository;

    public HabitsCommand(Terminal terminal, Builder componentFlowBuilder, ListCrudRepository<Habits, String> repository, HabitsRepository habitsRepository, Habits entity, DemographicsRepository demographicsRepository) 
    {
        super(terminal, componentFlowBuilder, repository, entity);
        this.demographicsRepository = demographicsRepository;
        this.habitsRepository = habitsRepository;
    }

    @Override
    @Command(command = {"new"}, description = "Создать")
    public void create() throws Exception
    {
        var item = ShellUtils.askForItem(componentFlowBuilder, entity.clone());
        if(demographicsRepository.findById(item.getUserId()).isPresent())
        {
            var newItem = repository.save(item);
            if(newItem != null)
                terminal.writer().println(ShellUtils.createItemFieldsTable(newItem).render(0));
            else
                terminal.writer().println("Не удалось получить созданный объект");
        }
        else
        {
            throw new Exception("Не наиден опрошенный с идентификатором " + item.getUserId());
        }
    }

    @Override
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
                var itemToNew = ShellUtils.askForItem(componentFlowBuilder, items.get());
                if(demographicsRepository.findById(itemToNew.getUserId()).isPresent())
                {
                    var newItem = repository.save(itemToNew);
                    if(newItem != null)
                        terminal.writer().println(ShellUtils.createItemFieldsTable(newItem).render(0));
                    else
                        terminal.writer().println("Не удалось получить созданный объект");
                }
                else
                {
                    throw new Exception("Не наиден опрошенный с идентификатором " + itemToNew.getUserId());
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

    // @Command(command = "average", description = "Среднее по опрошенным")
    // public void average()
    // {
    //     var items = habitsRepository.findAveragesGroupedByUserId();
    //     terminal.writer().println(ShellUtils.createTable(items, new HabitsAggregationResult(), 0).render(0));
    // }

    @Command(command = "filter")
    public void filter(
        @Option(longNames = "leftYear", required = true) int leftYear, 
        @Option(longNames = "rightYear", required = true) int rightYear,
        @Option(longNames = "physicalActivityDays", required = true) double physicalActivityDays
    )
    {
        var items = habitsRepository.findTask(leftYear, rightYear, physicalActivityDays);
        terminal.writer().println(ShellUtils.createTable(items, new Habits(), 0).render(0));
    }
}
