package ru.ssau.lab1new.view;

import java.util.UUID;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab1new.pojo.ProcessCountPojo;
import ru.ssau.lab1new.pojo.ProcessFileFullPojo;
import ru.ssau.lab1new.service.ProcessFileService;
import ru.ssau.lab1new.service.ServiceException;
import ru.ssau.lab1new.view.custom.CustomUtils;

@Command(group = "Действия с файлами процесса")
@RequiredArgsConstructor
public class ProcessFileCommandGroup 
{
    private final Terminal terminal;
    private final ComponentFlow.Builder componentFlowBuilder;
    private final ProcessFileService service;

    @Command(command = {"files"}, description = "Вывести список файлов процессов")
    public void listProcessFiles(
        @Option(longNames = "processId", shortNames = 'i', defaultValue = "") String id,
        @Option(longNames = "long", shortNames = 'l') boolean isLong,
        @Option(longNames = "write", shortNames = 'w') boolean isWrite,
        @Option(longNames = "read", shortNames = 'r') boolean isRead
    ) throws ServiceException
    {
        if(id == null || id.equals(""))
        {
            if (isLong) 
            {
                var files = service.listFiles();
                terminal.writer().println(CustomUtils.createTable(files, new ProcessFileFullPojo(), 1).render(100));
            }
            else
            {
                if(isWrite)
                {
                    var files = service.listFiles("Write");
                    terminal.writer().println(CustomUtils.createTable(files, new ProcessCountPojo(), 0).render(100));
                }
                else if(isRead)
                {
                    var files = service.listFiles("Read");
                    terminal.writer().println(CustomUtils.createTable(files, new ProcessCountPojo(), 0).render(100));
                }
                else
                {
                    var files = service.listFiles(null);
                    terminal.writer().println(CustomUtils.createTable(files, new ProcessCountPojo(), 0).render(100));
                }
            }
        }
        else
        {
            var files = service.listProcessFiles(UUID.fromString(id));
            terminal.writer().println(CustomUtils.createTable(files, new ProcessFileFullPojo(), 1).render(100));
        }
    }

    @Command(command = {"open"}, description = "Открыть файл в процессе")
    public void openProcessFile(
        @Option(longNames = "processId", shortNames = 'i', defaultValue = "", required = true) String processId,
        @Option(longNames = "path", shortNames = 'p', defaultValue = "", required = true) String path,
        @Option(longNames = "read", shortNames = 'r', defaultValue = "") boolean read,
        @Option(longNames = "write", shortNames = 'w', defaultValue = "") boolean write
    ) throws ServiceException
    {
        if(!read && !write)
        {
            terminal.writer().println("Укажите тип доступа к файлу");
        }
        else
        {
            var files = service.openFile(UUID.fromString(processId), path, read, write);
            terminal.writer().println(CustomUtils.createTable(files, new ProcessFileFullPojo(), 1).render(100));
        }
    }

    @Command(command = {"close"}, description = "Закрыть файл в процессе")
    public void closeProcessFile(
        @Option(longNames = "id", shortNames = 'i', defaultValue = "") String id
    ) throws ServiceException
    {
        var file = service.closeFile(UUID.fromString(id));
        terminal.writer().println(CustomUtils.createItemTable(file).render(100));
    }
}
