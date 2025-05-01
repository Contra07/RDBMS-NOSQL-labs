package ru.ssau.lab1new.view;

import java.util.Map;
import java.util.UUID;

import org.jline.terminal.Terminal;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.ssau.lab1new.pojo.ProcessFullPojo;
import ru.ssau.lab1new.pojo.ProcessPojo;
import ru.ssau.lab1new.service.ProcessService;
import ru.ssau.lab1new.service.ServiceException;
import ru.ssau.lab1new.view.custom.CustomUtils;
import ru.ssau.lab1new.view.custom.DurationFormatter;
import ru.ssau.lab1new.view.custom.TableHeader;

@Command(group = "Действия с процессами")
@RequiredArgsConstructor
public class ProcessCommandGroup 
{
    private final Terminal terminal;
    private final ComponentFlow.Builder componentFlowBuilder;
    private final ProcessService service;

    @Command(command = {"ps"}, description = "Вывести список процессов")
    public void listProcess(
        @Option(longNames = "id", defaultValue = "") String id,
        @Option(longNames = "long", shortNames = 'l') boolean isLong
    ) throws ServiceException
    {
        
        if (id == null || id.equals("")) 
        {
            var processes = service.listProcesses();
            if(isLong)
            {
                terminal.writer().println(
                    CustomUtils.createTable(processes, new ProcessFullPojo(), 0).render(100)
                );
            }
            else
            {
                var durFormatter = new DurationFormatter();
                terminal.writer().println(
                    String.format(
                        "%-45s%-30s%-15s", 
                        "Идентификатор",
                        "Имя",
                        "Время работы"
                    )
                );
                for (ProcessFullPojo process : processes) 
                {
                    terminal.writer().println(
                        String.format("%-40.40s : ", process.getId().toString()) +
                        String.format("%-" + process.getDepth()*3 + "s%-" + (30 - process.getDepth()*3) + "s : ", "", process.getName()) +
                        String.format("%-15.15s", durFormatter.format(process.getOperatingTime())[0])
                    );
                }
            }
        }
        else
        {
            var process = service.showProcess(UUID.fromString(id));
            terminal.writer().println(CustomUtils.createItemTable(process));
        }
    }

    @Command(command = {"run"}, description = "Запустить процесс")
    public void startProcess(
        @Option(longNames = "file", shortNames = 'f', defaultValue = "", required = true) String fullPath,
        @Option(longNames = "parent", shortNames = 'p', defaultValue = "") String parentId,
        @Option(longNames = "name", shortNames = 'n', required = true) String name,
        @Option(longNames = "arguments", shortNames = 'a') String arguments
    ) throws ServiceException
    {
        var process = service.startProcess(
            name, parentId != null ? UUID.fromString(parentId) : null, fullPath, arguments != null ? arguments : ""
        );
        terminal.writer().println(CustomUtils.createItemTable(process).render(80));
    }

    @Command(command = {"kill"}, description = "Остановить процесс и его дочерние процессы")
    public void startProcess(
        @Option(longNames = "id", defaultValue = "") String id
    ) throws ServiceException
    {
        Map<ProcessPojo, Integer> processes = service.stopProcess(UUID.fromString(id));
        terminal.writer().println("Завершены процессы: ");
        for (var process : processes.entrySet()) 
        {
            terminal.writer().println(CustomUtils.createItemTable(new DeleteTable(process.getKey().getId(), process.getKey().getName(), process.getValue())).render(80));
        }
    }

    @Data
    @AllArgsConstructor
    private class DeleteTable 
    {
        @TableHeader(name = "Идентификатор")
        UUID id;
        @TableHeader(name = "Имя")
        String name;
        @TableHeader(name = "Закрыто файлов")
        int files;  
    }
}
