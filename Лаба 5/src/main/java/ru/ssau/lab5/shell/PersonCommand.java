package ru.ssau.lab5.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow.Builder;

import ru.ssau.lab5.model.Person;
import ru.ssau.lab5.model.PersonWithCity;
import ru.ssau.lab5.repository.PersonRepository;
import ru.ssau.shared.shell.AbstractCommand;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = "person", group = "Опрошенный")
public class PersonCommand extends AbstractCommand
{
    private final PersonRepository repository;

    public PersonCommand(Terminal terminal, Builder componentFlowBuilder, PersonRepository repository) 
    {
        super(terminal, componentFlowBuilder);
        this.repository = repository;
    }

    @Command(command = "list")
    public void list()
    {
        var items = repository.findAll().stream().toList();
        terminal.writer().println(ShellUtils.createTable(items, new Person(), 0).render(0));
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
    public void create(
        @Option(longNames = "city", shortNames = 'c') Boolean withCity
    )
    {
        if(!withCity)
        {
            var person = ShellUtils.askForItem(componentFlowBuilder, new Person());
            var c = repository.merge(person);
            terminal.writer().println("Добавлено узлов: " + c);
        }
        else
        {
            var person = ShellUtils.askForItem(componentFlowBuilder, new PersonWithCity());
            var c = repository.mergeWithCity(person);
            terminal.writer().println("Добавлено узлов: " + c);
        }
    }

    @Command(command = "edit")
    public void edit(
        @Option(longNames = "name", shortNames = 'n', required = true) String name
    )
    {   var person = repository.findByName(name);
        if(person.isPresent())
        {
            var newPerson = repository.set(ShellUtils.askForItem(componentFlowBuilder, person.get()));
            terminal.writer().println(ShellUtils.createItemFieldsTable(newPerson.get()).render(0));
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