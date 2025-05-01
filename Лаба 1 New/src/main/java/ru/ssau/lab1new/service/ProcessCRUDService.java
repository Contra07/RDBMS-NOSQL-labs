package ru.ssau.lab1new.service;

import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab1new.model.Process;
import ru.ssau.lab1new.pojo.ProcessPojo;

@Service
@RequiredArgsConstructor
public class ProcessCRUDService implements CRUDService<ProcessPojo, UUID>
{
    private final CrudRepository<Process, UUID> repository;

    @Override
    public ProcessPojo Create(ProcessPojo pojo) throws ServiceException 
    {
        try 
        {
            return ProcessPojo.fromEntity(
                repository.save(ProcessPojo.toEntity(pojo))
            );
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка создания процесса с идентификатором " + pojo.getId(), e);
        }
    }

    @Override
    public ProcessPojo Read(UUID id) throws ServiceException 
    {
        try 
        {
            var process = repository.findById(id);
            if(process.isPresent())
                return ProcessPojo.fromEntity(process.get());
            else
                return null;
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка чтения процесса с идентификатором " + id, e);
        }
    }

    @Override
    public Iterable<ProcessPojo> ReadAll() throws ServiceException 
    {
        try 
        {
            return StreamSupport.stream(
                repository.findAll().spliterator(), false
            )
            .map(
                item -> {
                    return ProcessPojo.fromEntity(item);
                }
            ).toList();
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка чтения процессов", e);
        }
    }

    @Override
    public ProcessPojo Update(ProcessPojo pojo) throws ServiceException 
    {
        try 
        {
            return ProcessPojo.fromEntity(
                repository.save(ProcessPojo.toEntity(pojo))
            );
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка сохранения процесса с идентификатором " + pojo.getId(), e);
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
            throw new ServiceException("Ошибка удаления процесса с идентификатором " + id, e);
        }
    }

}
