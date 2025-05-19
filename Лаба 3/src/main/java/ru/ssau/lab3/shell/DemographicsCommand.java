package ru.ssau.lab3.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow.Builder;

import ru.ssau.lab3.model.Demographics;
import ru.ssau.lab3.repository.crud.DemographicsRepository;
import ru.ssau.lab3.repository.crud.HabitsRepository;
import ru.ssau.lab3.repository.crud.MentalStatsRepository;

@Command(command = "people", description = "CRUD люди")
public class DemographicsCommand extends CrudCommand<Demographics>
{
    private final HabitsRepository habitsRepository;
    private final MentalStatsRepository statsRepository;

    public DemographicsCommand(
        Terminal terminal, 
        Builder componentFlowBuilder, 
        DemographicsRepository repository, 
        HabitsRepository habitsRepository, 
        MentalStatsRepository statsRepository, 
        Demographics entity
    ) 
    {
        super(terminal, componentFlowBuilder, repository, entity);
        this.habitsRepository = habitsRepository;
        this.statsRepository = statsRepository;
    }

    @Override
    @Command(command = {"delete"}, description = "Удалить")
    public void delete(
        @Option(longNames = "id", shortNames = 'i') Integer id
    ) throws Exception
    {
        if (id != null)
        {
            var deleted = habitsRepository.deleteByUser(id);
            deleted += statsRepository.deleteByUser(id);
            deleted += repository.deleteById(id);
            terminal.writer().println("Удалено ключей: " + deleted);
        }
        else
        {
            throw new Exception("Не указан идентификатор");
        }
    }
}
