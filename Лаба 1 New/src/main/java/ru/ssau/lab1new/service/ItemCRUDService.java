package ru.ssau.lab1new.service;

import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab1new.model.Item;
import ru.ssau.lab1new.pojo.ItemPojo;

@Service
@RequiredArgsConstructor
public class ItemCRUDService implements CRUDService<ItemPojo, UUID> 
{
    private final CrudRepository<Item, UUID> repository;

    @Override
    public ItemPojo Create(ItemPojo entity) throws ServiceException 
    {
        try 
        {
            return ItemPojo.fromEntity(
                repository.save(ItemPojo.toEntity(entity))
            );
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка создания элемента в файловой системы с идентификатором " + entity.getId(), e);
        }
    }

    @Override
    public ItemPojo Read(UUID id) throws ServiceException 
    {
        try 
        {
            var item = repository.findById(id);
            return item.isPresent() ? ItemPojo.fromEntity(item.get()) : null;
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка получения элемента файловой системы с идентификатором " + id, e);
        }
    }

    @Override
    public Iterable<ItemPojo> ReadAll() throws ServiceException 
    {
        try 
        {
            return StreamSupport.stream(
                repository.findAll().spliterator(), false
            )
            .map(
                item -> {
                    return ItemPojo.fromEntity(item);
                }
            ).toList();
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка получения элементов файловой системы", e);
        }
    }

    @Override
    public ItemPojo Update(ItemPojo entity) throws ServiceException 
    {
        try 
        {
            return ItemPojo.fromEntity(
                repository.save(ItemPojo.toEntity(entity))
            );
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка сохранения элемента в файловой системы с идентификатором " + entity.getId(), e);
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
            throw new ServiceException("Ошибка удаления элемента файловой системы с идентификатором " + id, e);
        }
    }
}
