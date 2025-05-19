package ru.ssau.lab6.shell;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.jline.terminal.Terminal;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow.Builder;

import ru.ssau.lab6.model.Habits;
import ru.ssau.lab6.model.MentalStats;
import ru.ssau.lab6.repository.DemographicsRepository;
import ru.ssau.lab6.repository.MentalStatsRepository;
import ru.ssau.shared.shell.AbstractCommand;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = "stats", group = "Статистика")
public class MentalStatsCommand extends AbstractCommand
{
    private final MentalStatsRepository repository;
    private final DemographicsRepository demographicsRepository;

    public MentalStatsCommand(Terminal terminal, Builder componentFlowBuilder, MentalStatsRepository repository,
            DemographicsRepository demographicsRepository) {
        super(terminal, componentFlowBuilder);
        this.repository = repository;
        this.demographicsRepository = demographicsRepository;
    }

    @Command(command = "list")
    public void list(
        @Option(longNames = "person", shortNames = 'p') UUID id
    )
    {
        if(id == null)
        {
            var items = repository.findAll();
            terminal.writer().println(ShellUtils.createTable(items, new MentalStats(), 0).render(0));
        }
        else
        {
            var umapid = BasicMapId.id()
                .with("respondent_id", id);
            var items = repository.findAllById(List.of(umapid));
            terminal.writer().println(ShellUtils.createTable(items, new MentalStats(), 0).render(0));
        }
    }

    @Command(command = "show")
    public void show(
        @Option(longNames = "person", shortNames = 'p', required = true) UUID pId,
        @Option(longNames = "id", shortNames = 'i', required = true) UUID id
    )
    {
        var umapid = BasicMapId.id()
            .with("id", id)
            .with("respondentId", pId);
        var item = repository.findById(umapid);
        if(item.isPresent())
            terminal.writer().println(ShellUtils.createItemFieldsTable(item.get()).render(0));
    }

    @Command(command = "date")
    public void date(
        @Option(longNames = "date", shortNames = 'd', required = true) String date,
        @Option(longNames = "greater") boolean greater
    )
    {
        if (!greater) 
        {
            var items = repository.findByDate(LocalDate.parse(date));
            terminal.writer().println(ShellUtils.createTable(items, new Habits(), 0).render(0));
        }
        else
        {   
            var items = repository.findByGreaterDate(LocalDate.parse(date));
            terminal.writer().println(ShellUtils.createTable(items, new Habits(), 0).render(0));
        }
    }

    @Command(command = "new")
    public void create()
    {
        var item = ShellUtils.askForItem(componentFlowBuilder, new MentalStats());
        var p = demographicsRepository.findByRespondentId(item.getRespondentId());
        if(p.isPresent())
        {
            item.setId(UUID.randomUUID());
            item = repository.save(item);
            terminal.writer().println(ShellUtils.createItemFieldsTable(item).render(0));
        }
        else
        {
            terminal.writer().println("Отсутствует опрошенный с id " + item.getRespondentId());
        }
    }

    @Command(command = "edit")
    public void edit(
        @Option(longNames = "person", shortNames = 'p', required = true) UUID pId,
        @Option(longNames = "id", shortNames = 'i', required = true) UUID id
    )
    {
        var umapid = BasicMapId.id()
            .with("id", id)
            .with("respondentId", pId);
        var item = repository.findById(umapid);
        if(item.isPresent())
        {
            var newItem = ShellUtils.askForItem(componentFlowBuilder, item.get());
            var p = demographicsRepository.findByRespondentId(newItem.getRespondentId());
            if(p.isPresent())
            {
                newItem = repository.save(newItem);
                terminal.writer().println(ShellUtils.createItemFieldsTable(newItem).render(0));
            }
            else
            {
                terminal.writer().println("Отсутствует опрошенный с id " + newItem.getRespondentId());
            }
        }
    }

    @Command(command = "delete")
    public void delete(
        @Option(longNames = "person", shortNames = 'p', required = true) UUID pId,
        @Option(longNames = "id", shortNames = 'i') UUID id
    )
    {
        if (id == null) 
        {
            var umapid = BasicMapId.id()
                .with("respondent_id", pId);
            repository.deleteById(umapid);
        }
        else
        {
            var umapid = BasicMapId.id()
                .with("id", id)
                .with("respondentId", pId);
            repository.deleteById(umapid);
        }
    }
}
