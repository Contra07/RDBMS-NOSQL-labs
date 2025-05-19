package ru.ssau.lab3.shell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab3.model.KeyValuePair;
import ru.ssau.lab3.repository.hash.HashStringRepository;
import ru.ssau.shared.shell.ShellUtils;

@RequiredArgsConstructor
@Command(command = {"hash"}, group = "Хеш-таблица")
public class HashCommand 
{
    protected static final String NULL = "NULL";
    protected final Terminal terminal;
    protected final ComponentFlow.Builder componentFlowBuilder;
    protected final HashStringRepository repository;

    @Command(command = "get", description = "Получить элементы")
    public void get(
        @Option(longNames = "field", shortNames = 'f') String[] fields,
        @Option(longNames = "keys", shortNames = 'k') Boolean isKeys,
        @Option(longNames = "values", shortNames = 'v') Boolean isValues
    )
    {
        if(isKeys)
        {
            var items = repository.getKeys();
            if(items.size() > 0)
                terminal.writer().println(ShellUtils.createCollectionTable(items).render(0));
        }
        else if (isValues)
        {
            var items = repository.getValues();
            if(items.size() > 0)
                terminal.writer().println(ShellUtils.createCollectionTable(items).render(0));
        }
        else if(fields != null && fields.length == 1)
        {
            var item = repository.get(fields[0]);
            if(item != null)
                terminal.writer().println(ShellUtils.createCollectionTable(List.of(item)).render(0));
        }
        else if(fields != null && fields.length > 0)
        {
            var items = repository.get(List.of(fields));
            if(items.size() > 0)
                terminal.writer().println(ShellUtils.createCollectionTable(items).render(0));
        }
        else
        {
            var entries = repository.getEntries();
            List<KeyValuePair> items = entries
                .entrySet()
                .stream()
                .map(entry -> new KeyValuePair(entry.getKey(), entry.getValue()))
                .toList();
            if(items.size() > 0)
                terminal.writer().println(ShellUtils.createTable(items, new KeyValuePair(), 0).render(0));
        }
    }

    @Command(command = "put", description = "Добавить элементы")
    public void put(
        @Option(longNames = "element", shortNames = 'e') String[] elements,
        @Option(longNames = "absent", shortNames = 'a') Boolean ifAbsent
    )
    {
        if(elements != null && elements.length > 0)
        {
            if(elements.length == 2)
            {
                if(ifAbsent)
                {
                    var result = repository.putIfAbsent(elements[0], elements[1]);
                    if(result)
                    {
                        get(null, false, false);
                    }
                    else
                    {
                        terminal.writer().println("ЗАпись с данным ключом уже существует");
                    }
                }
                else
                {
                    repository.put(elements[0], elements[1]);
                    get(null, false, false);
                }
            }
            else if (elements.length % 2 == 0)
            {
                Map<Object, Object> en = new HashMap<>();
                for (int i = 0; i < elements.length; i+=2) 
                {
                    en.put(elements[i], elements[i+1]);
                }
                repository.putAll(en);
                get(null, false, false);
            }
        }
    }

    @Command(command = "delete", description = "Удалить элементы")
    public void delete(
        @Option(longNames = "key", shortNames = 'k', required = true) String[] keys
    )
    {
        if(keys != null && keys.length > 0)
        {
            var deleted = repository.delete(keys);
            terminal.writer().println("Удалено записей: " + deleted);
        }
    }

    @Command(command = "increment", description = "Увеличить значение")
    public void increment(
        @Option(longNames = "key", shortNames = 'k', required = true) String key,
        @Option(longNames = "delta", shortNames = 'd', required = true) Long delta
    )
    {
        var result = repository.increment(key, delta);
        terminal.writer().println("Новое значение: " + result);
    }

    @Command(command = "random", description = "Случайные элементы")
    public void random(
        @Option(longNames = "number", shortNames = 'n', required =  true) Integer count
    )
    {
        var randEntities = repository.random(count);
        List<KeyValuePair> items = randEntities
            .entrySet()
            .stream()
            .map(entry -> new KeyValuePair(entry.getKey(), entry.getValue()))
            .toList();
        if(items.size() > 0)
            terminal.writer().println(ShellUtils.createTable(items, new KeyValuePair(), 0).render(0));
    }
}
