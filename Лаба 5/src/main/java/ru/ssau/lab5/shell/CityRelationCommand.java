package ru.ssau.lab5.shell;

import java.time.LocalDate;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow.Builder;

import ru.ssau.lab5.model.PersonAndCityRelationShip;
import ru.ssau.lab5.repository.PersonCityRelationRepository;
import ru.ssau.shared.shell.AbstractCommand;
import ru.ssau.shared.shell.LocalDateFormatter;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = "city-relationship", group = "Связь опрошенного и города")
public class CityRelationCommand extends AbstractCommand
{
    private final PersonCityRelationRepository repository;

    public CityRelationCommand(Terminal terminal, Builder componentFlowBuilder, PersonCityRelationRepository repository) {
        super(terminal, componentFlowBuilder);
        this.repository = repository;
    }

    @Command(command = "list")
    public void city(
        @Option(longNames = "city", shortNames = 'c') String cityName,
        @Option(longNames = "person", shortNames = 'p') String personName
    )
    {
        if(cityName != null && personName != null)
        {
            var item = repository.find(cityName, personName);
            if(item.isPresent())
                terminal.writer().println(ShellUtils.createItemFieldsTable(item).render(0));
        }   
        else
        {
            var items = repository.findAll().stream().toList();
            terminal.writer().println(ShellUtils.createTable(items, new PersonAndCityRelationShip(), 0).render(0));
        }
    }

    @Command(command = "date")
    public void date(
        @Option(longNames = "date", shortNames = 'd') String date
    )
    {
        var ldate = LocalDate.parse(date);
        var items = repository.findByDate(ldate).stream().toList();
        terminal.writer().println(ShellUtils.createTable(items, new PersonAndCityRelationShip(), 0).render(0));
    }

    @Command(command = "new")
    public void create(
        @Option(longNames = "city", shortNames = 'c') Boolean withCity
    )
    {
        var relation = ShellUtils.askForItem(componentFlowBuilder, new PersonAndCityRelationShip());
        var c = repository.merge(relation);
        terminal.writer().println("Добавлено отношений: " + c);
    }

    @Command(command = "delete")
    public void delete()
    {
        var relation = ShellUtils.askForItem(componentFlowBuilder, new PersonAndCityRelationShip());
        var c = repository.delete(relation);
        terminal.writer().println("Удалено отношений: " + c);
    }
}
