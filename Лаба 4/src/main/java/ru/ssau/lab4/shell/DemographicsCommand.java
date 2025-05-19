package ru.ssau.lab4.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow.Builder;

import ru.ssau.lab4.model.Demographics;
import ru.ssau.lab4.repository.DemographicsRepository;
import ru.ssau.lab4.repository.HabitsRepository;
import ru.ssau.lab4.repository.MentalStatsRepository;

@Command(command = "people", description = "CRUD люди")
public class DemographicsCommand extends CrudCommand<Demographics>
{
    private final DemographicsRepository demographicsRepository;
    private final HabitsRepository habitsRepository;
    private final MentalStatsRepository statsRepository;

    public DemographicsCommand(Terminal terminal, Builder componentFlowBuilder, DemographicsRepository repository, Demographics entity, HabitsRepository habitsRepository, MentalStatsRepository statsRepository) 
    {
        super(terminal, componentFlowBuilder, repository, entity);
        this.habitsRepository = habitsRepository;
        this.statsRepository = statsRepository;
        this.demographicsRepository = repository;
    }

    @Override
    @Command(command = {"delete"}, description = "Удалить")
    public void delete(
        @Option(longNames = "id", shortNames = 'i') String id
    ) throws Exception
    {
        if (id != null)
        {
            habitsRepository.deleteByUser(id);
            statsRepository.deleteByUser(id);
            repository.deleteById(id);
        }
        else
        {
            throw new Exception("Не указан идентификатор");
        }
    }

    @Command(command = {"location"}, description = "Обновить место жительства")
    public void location(
        @Option(longNames = "old-location", shortNames = 'o') String oldLocation,
        @Option(longNames = "new-location", shortNames = 'n') String newLocation
    )
    {
        demographicsRepository.updateByLocation(oldLocation, newLocation);
    }
}