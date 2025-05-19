package ru.ssau.lab3.shell;

import java.util.List;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab3.repository.list.FirstListStringRepository;
import ru.ssau.lab3.repository.list.SecondListStringRepository;
import ru.ssau.shared.shell.ShellUtils;

@Command(command = {"name-list"}, group = "Список имён")
@RequiredArgsConstructor
public class NameListCommand 
{
    protected static final String NULL = "NULL";
    protected final Terminal terminal;
    protected final ComponentFlow.Builder componentFlowBuilder;
    protected final FirstListStringRepository firstRepository;
    protected final SecondListStringRepository secondRepository;

    @Command(command = "get", description = "Получить элементы")
    public void get(
        @Option(longNames = "start", shortNames = 's') Integer start,
        @Option(longNames = "end", shortNames = 'e') Integer end,
        @Option(longNames = "second", shortNames = 's') boolean isSecondList
    )
    {
        var repository = isSecondList ? secondRepository : firstRepository;
        if(start != null && end != null)
        {
            var items = repository.getRange(start, end);
            if(items.size() > 0)
                terminal.writer().println(ShellUtils.createCollectionTable(items).render(0));
        }
        else if (start != null)
        {
            var item = repository.get(start);
            if(item != null)
                terminal.writer().println(ShellUtils.createCollectionTable(List.of(item)).render(0));
        }
        else
        {
            var items = repository.getRange(0, -1);
            if(items.size() > 0)
                terminal.writer().println(ShellUtils.createCollectionTable(items, true).render(0));
        }
    }

    @Command(command = "size", description = "Размер списка")
    public void size(
    @Option(longNames = "second", shortNames = 's') boolean isSecondList
    )
    {
        var repository = isSecondList ? secondRepository : firstRepository;
        var size = repository.getSize();
        terminal.writer().println(ShellUtils.createCollectionTable(List.of(size)).render(0));
    }

    @Command(command = "push", description = "Добавить элементы")
    public void add(
        @Option(longNames = "values", shortNames = 'v') String[] values,
        @Option(longNames = "left", shortNames = 'l') boolean isLeft,
        @Option(longNames = "right", shortNames = 'r') boolean isRight,
        @Option(longNames = "second", shortNames = 's') boolean isSecondList
    )
    {
        var repository = isSecondList ? secondRepository : firstRepository;
        if(values != null && values.length > 0)
        {
            if(isLeft)
            {
                var n = repository.putLeft(values);
                System.out.println("Добавлено значений: " + n);
            }
            else if (isRight)
            {
                var n = repository.putRight(values);
                System.out.println("Добавлено значений: " + n);   
            }
            else
            {
                var n = repository.putLeft(values);
                System.out.println("Добавлено значений: " + n);
            }
            get(null, null, isSecondList);
        }
    }

    @Command(command = "pop", description = "Извлечь элементы")
    public void delete(
        @Option(longNames = "left", shortNames = 'l') boolean isLeft,
        @Option(longNames = "right", shortNames = 'r') boolean isRight,
        @Option(longNames = "number", shortNames = 'n') Integer count,
        @Option(longNames = "second", shortNames = 's') boolean isSecondList
    )
    {
        var repository = isSecondList ? secondRepository : firstRepository;
        var n = count == null ? 1 : count;
        if(isRight)
        {
            var items = repository.popRight(n);
            if(items.size() > 0)
                terminal.writer().println(ShellUtils.createCollectionTable(items, true).render(0));
        }
        else if (isLeft) 
        {
            var items = repository.popLeft(n);
            if(items.size() > 0)
                terminal.writer().println(ShellUtils.createCollectionTable(items, true).render(0));
        }
        else
        {
            var items = repository.popLeft(n);
            if(items.size() > 0)
                terminal.writer().println(ShellUtils.createCollectionTable(items, true).render(0));
        }
        get(null, null, isSecondList);
    }

    @Command(command = "trim", description = "Обрезать список")
    public void trim(
        @Option(longNames = "start", shortNames = 's', required = true) Integer start,
        @Option(longNames = "end", shortNames = 'e', required = true) Integer end,
        @Option(longNames = "second", shortNames = 's') boolean isSecondList
    )
    {
        var repository = isSecondList ? secondRepository : firstRepository;
        repository.trim(start, end);
        get(null, null, isSecondList);
    }

    @Command(command = "move", description = "Переместить список")
    public void move(
        @Option(longNames = "source-first") boolean sourceFirst,
        @Option(longNames = "source-last") boolean sourceLast,
        @Option(longNames = "destination-first") boolean destinationFirst,
        @Option(longNames = "destination-last") boolean destinationLast,
        @Option(longNames = "reverse") boolean reverse
    )
    {
        var sourceRepository = reverse ? secondRepository : firstRepository;
        var targetRepository = reverse ? firstRepository : secondRepository;
        var fromSourceFirst = sourceFirst && !sourceLast;
        var toTargetFirst = destinationFirst && !destinationLast;
        var item = sourceRepository.move(targetRepository.getKey(), fromSourceFirst, toTargetFirst);
        terminal.writer().println();
        terminal.writer().println(ShellUtils.createCollectionTable(List.of(item)).render(0));
        terminal.writer().println();
        get(null, null, false);
        terminal.writer().println();
        get(null, null, true);
    }
}
