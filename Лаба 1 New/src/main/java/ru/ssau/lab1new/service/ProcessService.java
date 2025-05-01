package ru.ssau.lab1new.service;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab1new.pojo.ProcessFullPojo;
import ru.ssau.lab1new.pojo.ProcessPojo;
import ru.ssau.lab1new.repository.ItemRepository;
import ru.ssau.lab1new.repository.ProcessRepository;

@Service
@RequiredArgsConstructor
public class ProcessService 
{
    private final ProcessRepository repository;
    private final ItemRepository itemRepository;

    public List<ProcessFullPojo> listProcesses() throws ServiceException
    {
        try 
        {
            return repository.findProcesses().stream()
            .map(
                process -> ProcessFullPojo.fromProjection(process)
            ).toList();
        }
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка получения процессов", e);
        }
    }

    public ProcessPojo showProcess(UUID id) throws ServiceException
    {
        try 
        {
            return ProcessPojo.fromProjection(repository.findProcess(id));
        }
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка получения процесса", e);
        }
    }

    public ProcessPojo startProcess(String name, UUID parent, String path, String arguments) throws ServiceException
    {
        try 
        {
            var item = itemRepository.findItem(path.split("/"));
            if(item == null)
            {
                throw new ServiceException("Не удалось найти файл: " + path);
            }
            if(item.getIsFolder())
            {
                throw new ServiceException("Нельзя запустить директорию: " + path);
            }
            if(parent != null && repository.findProcess(parent) == null)
            {
                throw new ServiceException("Не удалось найти процесс с идентификатором: " + parent);
            }
            return ProcessPojo.fromProjection(repository.createProcess(name, parent, path.split("/"), arguments.split(" ")));
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка запуска процесса", e);
        }
    }

    public Map<ProcessPojo, Integer> stopProcess(UUID id) throws ServiceException
    {
        try 
        {
            if(repository.findProcess(id) == null)
                throw new ServiceException("Не удалось найти процесс " + id);
            return repository.removeProcess(id).stream()
            .collect(
                Collectors.toMap(
                    process -> ProcessFullPojo.fromProjection(process),
                    process -> process.getFiles()
                )
            );
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка остановки процесса: " + id, e);
        }
    }
}
