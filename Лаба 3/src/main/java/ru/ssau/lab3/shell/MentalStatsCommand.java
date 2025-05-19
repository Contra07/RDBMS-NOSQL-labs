package ru.ssau.lab3.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow.Builder;

import ru.ssau.lab3.model.Habits;
import ru.ssau.lab3.model.MentalStats;
import ru.ssau.lab3.repository.crud.CrudRepository;
import ru.ssau.lab3.repository.crud.DemographicsRepository;
import ru.ssau.shared.shell.ShellUtils;


@Command(command = {"stats"}, group = "CRUD ментальный опрос")
public class MentalStatsCommand extends CrudCommand<MentalStats>
{
    private final DemographicsRepository demographicsRepository;
    public MentalStatsCommand(
        Terminal terminal, 
        Builder componentFlowBuilder, 
        CrudRepository<MentalStats> repository,
        DemographicsRepository demographicsRepository,
        MentalStats entity
    ) 
    {
        super(terminal, componentFlowBuilder, repository, entity);
        this.demographicsRepository = demographicsRepository;
    }

    @Override
    @Command(command = {"new"}, description = "Создать")
    public void create() throws Exception
    {
        var item = ShellUtils.askForItem(componentFlowBuilder, (MentalStats)entity.clone());
        if(demographicsRepository.findById(item.getUserId()).isPresent())
        {
            var newItem = repository.create(item);
            if(newItem.isPresent())
                terminal.writer().println(ShellUtils.createItemFieldsTable(newItem.get()).render(0));
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
        @Option(longNames = "id", shortNames = 'i', required = true) Integer id
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
                    var newItem = repository.edit(id, itemToNew);
                    if(newItem.isPresent())
                        terminal.writer().println(ShellUtils.createItemFieldsTable(newItem.get()).render(0));
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
}
