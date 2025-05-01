package ru.ssau.lab1new.service;

import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab1new.model.ProcessFile;
import ru.ssau.lab1new.pojo.ProcessFilePojo;

@Service
@RequiredArgsConstructor
public class ProcessFileCRUDService implements CRUDService<ProcessFilePojo, UUID>
{
    private final CrudRepository<ProcessFile, UUID> repository;

    @Override
    public ProcessFilePojo Create(ProcessFilePojo entity) throws ServiceException 
    {
        try 
        {
            return ProcessFilePojo.fromEntity(
                repository.save(ProcessFilePojo.toEntity(entity))
            );
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка создания связи файла процесса с идентификатором " + entity.getId(), e);
        }
    }

    @Override
    public ProcessFilePojo Read(UUID id) throws ServiceException 
    {
        try 
        {
            var entity = repository.findById(id);
            return entity.isPresent() ? ProcessFilePojo.fromEntity(entity.get()) : null;
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка получения связи файла процесса с идентификатором " + id, e);
        }
    }

    @Override
    public Iterable<ProcessFilePojo> ReadAll() throws ServiceException 
    {
        try 
        {
            return StreamSupport.stream(
                repository.findAll().spliterator(), false
            )
            .map(
                item -> {
                    return ProcessFilePojo.fromEntity(item);
                }
            ).toList();
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка получения связей файлов и процессов", e);
        }
    }

    @Override
    public ProcessFilePojo Update(ProcessFilePojo entity) throws ServiceException 
    {
        try 
        {
            return ProcessFilePojo.fromEntity(
                repository.save(ProcessFilePojo.toEntity(entity))
            );
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка сохранения связей файла и процесса с идентификатором " + entity.getId(), e);
        }
    }

    @Override
    public void Delete(UUID id) throws ServiceException 
    {
        try 
        {
            repository.deleteById(id);
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка удаления связей файла и процесса с идентификатором " + id, e);
        }
    }

}
