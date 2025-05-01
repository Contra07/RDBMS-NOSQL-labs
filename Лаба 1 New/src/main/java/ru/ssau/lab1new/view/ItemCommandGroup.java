package ru.ssau.lab1new.view;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab1new.pojo.ItemComparePojo;
import ru.ssau.lab1new.pojo.ItemFullPathPojo;
import ru.ssau.lab1new.service.ItemService;
import ru.ssau.lab1new.service.ServiceException;
import ru.ssau.lab1new.view.custom.CustomUtils;

@Command(group = "Действия с файлами и каталогами")
@RequiredArgsConstructor
public class ItemCommandGroup 
{
    private final Terminal terminal;
    private final ComponentFlow.Builder componentFlowBuilder;
    private final ItemService service;

    @Command(command = {"ls"}, description = "Вывести список файлов/каталогов")
    public void list(
        @Option(defaultValue = "", longNames = "path", shortNames = 'p') String path, 
        @Option(longNames = "long", shortNames = 'l') boolean longList, 
        @Option(longNames = "recursive", shortNames = 'r') boolean recurse
    ) throws ServiceException
    {
        var items = service.listItems(path == null ? "" : path, recurse);
        if (items.size() == 0) 
        {
            terminal.writer().println("Отсутствуют файлы по пути: " + path);
        }
        else
        {
            if (!longList) 
            {
                for (var item : items) 
                {
                    if(recurse)
                        terminal.writer().println(item.getFullPath());
                    else
                        terminal.writer().println(item.getName());
                }
            }
            else
            {
                terminal.writer().println(CustomUtils.createTable(items, new ItemFullPathPojo(), 1).render(100));
            }
        }
    }

    @Command(command = {"touch"}, description = "Создать файл")
    public void touchFile(
        @Option(longNames = "path", shortNames = 'p', required = true) String path, 
        @Option(longNames = "size", shortNames = 's', required = true) int size, 
        @Option(longNames = "recursive", shortNames = 'r') boolean recurse
    ) throws ServiceException
    {
        var insertItems =  service.createItem(path == null ? "" : path, ((long)size), false, recurse);
        terminal.writer().println("Добавлено элементов: " + insertItems);
    }

    @Command(command = {"mkdir"}, description = "Создать папку")
    public void makeDirectory(
        @Option(longNames = "path", shortNames = 'p', required = true) String path, 
        @Option(longNames = "recursive", shortNames = 'r') boolean recurse
    ) throws ServiceException
    {
        var insertItems =  service.createItem(path == null ? "" : path, Long.valueOf(0), true, recurse);
        terminal.writer().println("Добавлено элементов: " + insertItems);
    }

    @Command(command = {"rm"}, description = "Удалить файл или папку")
    public void removeItem(
        @Option(longNames = "path", shortNames = 'p', required = true) String path, 
        @Option(longNames = "recursive", shortNames = 'r') boolean recurse
    ) throws ServiceException
    {
        var deletedItems = service.removeItem(path, recurse);
        terminal.writer().println("Удалено элементов: " + deletedItems);
    }

    @Command(command = {"diff"}, description = "Сравнить файлы в папках по имени")
    public void diffItems(
        @Option(longNames = "fisrtpath", shortNames = 'f', required = true) String path1, 
        @Option(longNames = "secondpath", shortNames = 's', required = true) String path2
    ) throws ServiceException
    {
        var items = service.diffFiles(path1, path2);
        terminal.writer().println(CustomUtils.createTable(items, new ItemComparePojo(), 0).render(100));
    }
}
