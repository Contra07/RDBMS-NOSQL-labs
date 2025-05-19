package ru.ssau.lab5.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow.Builder;

import ru.ssau.lab5.model.City;
import ru.ssau.lab5.model.CityNameAndAnxietyLevel;
import ru.ssau.lab5.model.CityNameAndCount;
import ru.ssau.lab5.repository.CityRepository;
import ru.ssau.shared.shell.AbstractCommand;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = "city", group = "Город")
public class CityCommand extends AbstractCommand
{
    private final CityRepository repository;

    public CityCommand(Terminal terminal, Builder componentFlowBuilder, CityRepository repository) 
    {
        super(terminal, componentFlowBuilder);
        this.repository = repository;
    }

    @Command(command = "list")
    public void list()
    {
        var items = repository.findAll().stream().toList();
        terminal.writer().println(ShellUtils.createTable(items, new City(), 0).render(0));
    }

    @Command(command = "persons")
    public void persons(
        @Option(longNames = "count", shortNames = 'c', required = true) Integer personCount
    )
    {
        var items = repository.findByCount(personCount).stream().toList();
        terminal.writer().println(ShellUtils.createTable(items, new CityNameAndCount(), 0).render(0));
    }

    @Command(command = "anxiety")
    public void anxiety(
        @Option(longNames = "level", shortNames = 'l', required = true) Double avgAnxietyLevel
    )
    {
        var items = repository.findByAnxietyLevel(avgAnxietyLevel).stream().toList();
        terminal.writer().println(ShellUtils.createTable(items, new CityNameAndAnxietyLevel(), 0).render(0));
    }

    @Command(command = "show")
    public void show(
        @Option(longNames = "name", shortNames = 'n', required = true) String name
    )
    {
        var item = repository.findByName(name);
        if(item.isPresent())
        {
            terminal.writer().println(ShellUtils.createItemFieldsTable(item.get()).render(0));
        }
    }

    @Command(command = "new")
    public void create()
    {
        var person = ShellUtils.askForItem(componentFlowBuilder, new City());
        var c = repository.merge(person);
        terminal.writer().println("Добавлено узлов: " + c);
    }

    @Command(command = "edit")
    public void edit(
        @Option(longNames = "name", shortNames = 'n', required = true) String name
    )
    {   var person = repository.findByName(name);
        if(person.isPresent())
        {
            var newPerson = repository.set(name, ShellUtils.askForItem(componentFlowBuilder, person.get()));
            if(newPerson.isPresent())
            {
                terminal.writer().println(ShellUtils.createItemFieldsTable(newPerson.get()).render(0));
            }
        }
    }

    @Command(command = "delete")
    public void delete(
        @Option(longNames = "name", shortNames = 'n', required = true) String name
    )
    {
        var c = repository.delete(name);
        terminal.writer().println("Удалено узлов: " + c);
    }
}