package ru.ssau.lab6.shell;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jline.terminal.Terminal;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow.Builder;

import ru.ssau.lab6.model.Respondents;
import ru.ssau.lab6.repository.DemographicsRepository;
import ru.ssau.lab6.repository.HabitsRepository;
import ru.ssau.lab6.repository.MentalStatsRepository;
import ru.ssau.shared.shell.AbstractCommand;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = "people", description = "Опрошенные")
public class DemographicsCommand extends AbstractCommand
{
    private final DemographicsRepository repository;
    private final HabitsRepository habitsRepository;
    private final MentalStatsRepository mentalStatsRepository;

    public DemographicsCommand(Terminal terminal, Builder componentFlowBuilder, DemographicsRepository repository,
            HabitsRepository habitsRepository, MentalStatsRepository mentalStatsRepository) {
        super(terminal, componentFlowBuilder);
        this.repository = repository;
        this.habitsRepository = habitsRepository;
        this.mentalStatsRepository = mentalStatsRepository;
    }

    @Command(command = "list")
    public void list(
        @Option(longNames = "city", shortNames = 'c') String city
    )
    {
        if(city != null)
        {
            var mapid = BasicMapId.id()
                .with("location", city);

            var items = repository.findAllById(List.of(mapid));
            terminal.writer().println(ShellUtils.createTable(items, new Respondents(), 0).render(0));
        }
        else
        {
            var items = repository.findAll();
            terminal.writer().println(ShellUtils.createTable(items, new Respondents(), 0).render(0));
        }
    }

    @Command(command = "show")
    public void show(
        @Option(longNames = "id", shortNames = 'i') UUID id,
        @Option(longNames = "city", shortNames = 'c') String city
    )
    {
        if(city != null && id != null)
        {
            var mapid = BasicMapId.id()
                .with("id", id)
                .with("location", city);
        
            var item = repository.findById(mapid);
            if(item.isPresent())
                terminal.writer().println(ShellUtils.createItemFieldsTable(item.get()).render(0));
        }
        else if (id != null)
        {
            var item = repository.findByRespondentId(id);
            if(item.isPresent())
                terminal.writer().println(ShellUtils.createItemFieldsTable(item.get()).render(0));
        }
        else
        {
            terminal.writer().println("Укажите id и city");
        }
    }

    @Command(command = "new")
    public void create()
    {
        var item = ShellUtils.askForItem(componentFlowBuilder, new Respondents());
        item.setId(UUID.randomUUID());
        item = repository.save(item);
        terminal.writer().println(ShellUtils.createItemFieldsTable(item).render(0));
    }

    @Command(command = "edit")
    public void edit(
        @Option(longNames = "id", shortNames = 'i') UUID id,
        @Option(longNames = "city", shortNames = 'c') String city
    )
    {
        var mapid = BasicMapId.id()
                .with("id", id)
                .with("location", city);

        var item = repository.findById(mapid);
        if(item.isPresent())
        {
            var newItem = ShellUtils.askForItem(componentFlowBuilder, item.get());
            newItem = repository.save(newItem);
            terminal.writer().println(ShellUtils.createItemFieldsTable(newItem).render(0));
        }
    }

    @Command(command = "delete")
    public void delete(
        @Option(longNames = "id", shortNames = 'i') UUID id,
        @Option(longNames = "city", shortNames = 'c') String city
    )
    {
        if(id != null || city != null) 
        {
            var mapid = BasicMapId.id();
            if(id != null && city != null)
            {
                mapid
                    .with("id", id)
                    .with("location", city);
            }
            else if(id != null)
            {
                var respondent = repository.findByRespondentId(id);
                if (respondent.isPresent()) {
                    mapid
                        .with("id", id)
                        .with("location", respondent.get().getLocation());
                }
            }
            else if(city != null)
            {
                mapid
                    .with("location", city);
            }

            List<MapId> umapids = new ArrayList<>();
            if(id == null)
            {
                umapids = repository
                    .findAllById(List.of(mapid))
                    .stream()
                    .map(r -> BasicMapId.id().with("respondent_id", r.getId()))
                    .toList();
            }
            else
            {
                var umapid = BasicMapId.id()
                    .with("respondent_id", id);
                umapids = List.of(umapid);
            }

            habitsRepository.deleteAllById(umapids);
            mentalStatsRepository.deleteAllById(umapids);
            repository.deleteAllById(List.of(mapid));
        }
    }

}
