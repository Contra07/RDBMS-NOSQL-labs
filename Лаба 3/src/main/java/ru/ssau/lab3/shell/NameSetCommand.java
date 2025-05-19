package ru.ssau.lab3.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab3.repository.list.FirstListStringRepository;
import ru.ssau.lab3.repository.list.SecondListStringRepository;
import ru.ssau.lab3.repository.set.FirstSetStringRepository;
import ru.ssau.lab3.repository.set.SecondSetStringRepository;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = {"name-set"}, group = "Множество имён")
@RequiredArgsConstructor
public class NameSetCommand 
{
    protected static final String NULL = "NULL";
    protected final Terminal terminal;
    protected final ComponentFlow.Builder componentFlowBuilder;
    protected final FirstSetStringRepository firstRepository;
    protected final SecondSetStringRepository secondRepository;
    protected final FirstListStringRepository firstListRepository;
    protected final SecondListStringRepository secondListRepository;

    @Command(command = "get", description = "Получить элементы")
    public void get(
        @Option(longNames = "number", shortNames = 'n') Integer number,
        @Option(longNames = "random", shortNames = 'r') boolean isRandom,
        @Option(longNames = "distinct", shortNames = 'd') boolean isDistinct,
        @Option(longNames = "second", shortNames = 's') boolean isSecondList
    )
    {
        var repository = isSecondList ? secondRepository : firstRepository;
        if(isRandom)
        {
            if(number == null)
            {
                var items = repository.getRandom(1, isDistinct);
                if(items.size() > 0)
                    terminal.writer().println(ShellUtils.createCollectionTable(items).render(0));
            }
            else
            {
                var items = repository.getRandom(number, isDistinct);
                if(items.size() > 0)
                    terminal.writer().println(ShellUtils.createCollectionTable(items).render(0));
            }
        }
        else
        {
            if(number == null)
            {
                var items = repository.getAll();
                if(items.size() > 0)
                    terminal.writer().println(ShellUtils.createCollectionTable(items).render(0));
            }
        }
        
        
    }

    @Command(command = "add", description = "Добавить элементы")
    public void add(
        @Option(longNames = "values", shortNames = 'v') String[] values,
        @Option(longNames = "second", shortNames = 's') boolean isSecondList
    )
    {
        var repository = isSecondList ? secondRepository : firstRepository;
        if(values != null && values.length > 0)
            repository.add(values);
        get(null, false, false, isSecondList);
    }

    @Command(command = "from-list", description = "Добавить элементы")
    public void fromList(
        @Option(longNames = "second", shortNames = 's') boolean isSecondList
    )
    {
        var repository = isSecondList ? secondRepository : firstRepository;
        repository.add(firstListRepository.getRange(0, -1).toArray(String[]::new));
        get(null, false, false, isSecondList);
    }

    @Command(command = "union", description = "Объединение")
    public void union()
    {
        var items = firstRepository.getUnion(secondRepository.getKey());
        if(items.size() > 0)
            terminal.writer().println(ShellUtils.createCollectionTable(items).render(0));
    }

    @Command(command = "intersect", description = "Пересечение")
    public void intersect()
    {
        var items = firstRepository.getIntersect(secondRepository.getKey());
        if(items.size() > 0)
            terminal.writer().println(ShellUtils.createCollectionTable(items).render(0));
    }

    @Command(command = "difference", description = "Разность")
    public void difference()
    {
        var items = firstRepository.getDifference(secondRepository.getKey());
        if(items.size() > 0)
            terminal.writer().println(ShellUtils.createCollectionTable(items).render(0));
    }
}
