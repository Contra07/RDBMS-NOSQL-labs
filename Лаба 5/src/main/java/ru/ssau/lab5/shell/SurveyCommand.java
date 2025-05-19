package ru.ssau.lab5.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow.Builder;

import ru.ssau.lab5.model.Person;
import ru.ssau.lab5.model.PersonWithCity;
import ru.ssau.lab5.model.Survey;
import ru.ssau.lab5.model.SurveyWithName;
import ru.ssau.lab5.repository.PersonRepository;
import ru.ssau.lab5.repository.SurveyRepository;
import ru.ssau.shared.shell.AbstractCommand;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = "survey", group = "Опросник")
public class SurveyCommand extends AbstractCommand
{
    private final SurveyRepository repository;

    public SurveyCommand(Terminal terminal, Builder componentFlowBuilder, SurveyRepository repository) 
    {
        super(terminal, componentFlowBuilder);
        this.repository = repository;
    }

    @Command(command = "list")
    public void list()
    {
        var items = repository.findAll().stream().toList();
        terminal.writer().println(ShellUtils.createTable(items, new Survey(), 0).render(0));
    }

    @Command(command = "show")
    public void show(
        @Option(longNames = "id", shortNames = 'i', required = true) Integer id
    )
    {
        var item = repository.findById(id);
        if(item.isPresent())
        {
            terminal.writer().println(ShellUtils.createItemFieldsTable(item.get()).render(0));
        }
    }

    @Command(command = "new")
    public void create(
        @Option(longNames = "name", shortNames = 'n') Boolean withName
    )
    {
        if(!withName)
        {
            var survey = ShellUtils.askForItem(componentFlowBuilder, new Survey());
            var c = repository.merge(survey);
            terminal.writer().println("Добавлено узлов: " + c);
        }
        else
        {
            var survey = ShellUtils.askForItem(componentFlowBuilder, new SurveyWithName());
            var c = repository.mergeWithName(survey.getName(), survey);
            terminal.writer().println("Добавлено узлов: " + c);
        }
    }

    @Command(command = "edit")
    public void edit(
        @Option(longNames = "id", shortNames = 'i', required = true) Integer id
    )
    {   var survey = repository.findById(id);
        if(survey.isPresent())
        {
            var newPerson = repository.set(ShellUtils.askForItem(componentFlowBuilder, survey.get()));
            terminal.writer().println(ShellUtils.createItemFieldsTable(newPerson.get()).render(0));
        }
    }

    @Command(command = "delete")
    public void delete(
        @Option(longNames = "id", shortNames = 'i', required = true) Integer id
    )
    {
        var c = repository.delete(id);
        terminal.writer().println("Удалено узлов: " + c);
    }
}
