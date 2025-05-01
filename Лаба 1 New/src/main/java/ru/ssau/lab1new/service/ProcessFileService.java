package ru.ssau.lab1new.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab1new.model.FileAccessType;
import ru.ssau.lab1new.pojo.ProcessCountPojo;
import ru.ssau.lab1new.pojo.ProcessFileFullPojo;
import ru.ssau.lab1new.repository.ItemRepository;
import ru.ssau.lab1new.repository.ProcessFileRepository;
import ru.ssau.lab1new.repository.ProcessRepository;

@Service
@RequiredArgsConstructor
public class ProcessFileService 
{
    private final ProcessRepository processRepository;
    private final ItemRepository itemRepository;
    private final ProcessFileRepository repository;

    public List<ProcessFileFullPojo> listFiles() throws ServiceException
    {   
        try 
        {
            return repository.readAllProcessFiles().stream()
                .map(processFile -> ProcessFileFullPojo.fromProjection(processFile))
                .toList();
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка получения списка файлов процессов", e);
        }
    }

    public List<ProcessFileFullPojo> listProcessFiles(UUID id) throws ServiceException
    {   
        try 
        {
            return repository.readProcessFiles(id).stream()
                .map(processFile -> ProcessFileFullPojo.fromProjection(processFile))
                .toList();
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка получения списка файлов процесса: " + id, e);
        }
    }

    public List<ProcessCountPojo> listFiles(String accessType) throws ServiceException
    {
        try 
        {
            if(accessType == null)
            {
                return repository.countAllProcessFiles().stream()
                    .map(processFile -> ProcessCountPojo.fromProjection(processFile))
                    .toList();
            }
            else
            {
                return repository.countAllTypeProcessFiles(accessType).stream()
                    .map(processFile -> ProcessCountPojo.fromProjection(processFile))
                    .toList();
            }
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка получения списка файлов процессов", e);
        }
    }
    
    public List<ProcessFileFullPojo> openFile(UUID processId, String filepath, boolean read, boolean write) throws ServiceException
    {
        try 
        {
            if(processId == null)
                throw new IllegalArgumentException("Идентификатор процесса не может быть пустым");
            if(filepath == null)
                throw new IllegalArgumentException("Пут к файлу не может быть пустым");
            if(processRepository.findProcess(processId) == null)
                throw new ServiceException("Не удалось найти процесс: " + processId);
            
            var item = itemRepository.findItem(filepath.split("/"));
            if(item == null)
                throw new ServiceException("Не удалось найти файл: " + filepath);
            if(item.getIsFolder())
                throw new ServiceException("Путь указывает на каталог: " + filepath);
            if(filepath == null || itemRepository.findItem(filepath.split("/")) == null)
                throw new ServiceException("Не удалось найти файл: " + processId);
            
            var accesses = new ArrayList<String>();
            if(read)
                accesses.add(FileAccessType.Read.name());
            if(write)
                accesses.add(FileAccessType.Write.name());
            var a = new String[accesses.size()];
            for (int i = 0; i < accesses.size(); i++) {
                a[i] = accesses.get(i);
            }
            return repository.createProcessFiles(processId, filepath.split("/"), a).stream()
                .map(processFile -> ProcessFileFullPojo.fromProjection(processFile))
                .toList();
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка открытия файла процесса", e);
        }
    }
    
    public ProcessFileFullPojo closeFile(UUID id) throws ServiceException
    {
        try 
        {
            if(id == null)
                throw new IllegalArgumentException("Идентификатор файла процесса не может быть пустым");
            
            return ProcessFileFullPojo.fromProjection(repository.removeProcessFile(id));
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка закрытия файла процесса", e);
        }
    }
}
