package ru.ssau.lab1new.service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab1new.pojo.ItemComparePojo;
import ru.ssau.lab1new.pojo.ItemFullPathPojo;
import ru.ssau.lab1new.repository.ItemRepository;

@Service
@RequiredArgsConstructor
public class ItemService 
{
    private final ItemRepository repository;
    public List<ItemFullPathPojo> listItems(String path, boolean recurse) throws ServiceException
    {
        try 
        {
            return StreamSupport.stream(
                !recurse
                    ? repository.findLevelItems(path.split("/")).spliterator()
                    : repository.findInnerItems(path.split("/")).spliterator(), 
                false
            )
            .map(
                item -> {
                    return ItemFullPathPojo.fromProjection(item);
                }
            ).toList();
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка получения элементов файловой системы", e);
        }
    }

    public int createItem(String path, Long size, boolean folder, boolean recurse) throws ServiceException
    {
        var paths = path.split("/");
        try 
        {
            var item = repository.findItem(paths);
            if(item != null)
                throw new ServiceException("Элемент уже создан: " + ItemFullPathPojo.fromProjection(item).getFullPath());
            
            if (recurse) 
            {
                return repository.insertItemRecursive(paths,folder, size).size();
            }
            else
            {
                return repository.insertItem(paths, folder, size) == null ? 0 : 1;
            }
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка создания элемента", e);
        }
    }

    public int removeItem(String path, boolean recurse) throws ServiceException
    {
        var paths = path.split("/");
        try 
        {
            var item = repository.findItem(paths);
            if(item == null)
                throw new ServiceException("Элемент отсутствует: " + path);
            
            if (!recurse) 
            {
                return repository.removeItem(paths) != null ? 1 : 0;
            }
            else
            {
                return repository.removeItemsRecursive(paths).size();
            }
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка удаления элемента", e);
        }
    }

    public List<ItemComparePojo> diffFiles(String path1, String path2) throws ServiceException
    {
        var paths1 = path1.split("/");
        var paths2 = path2.split("/");
        try 
        {
            return repository.compareElements(paths1, paths2)
                .stream()
                .map(
                item -> {
                    return ItemComparePojo.fromProjection(item);
                }
            ).toList();
        } 
        catch (DataAccessException e) 
        {
            throw new ServiceException("Ошибка получения элементов", e);
        }
    }
}
