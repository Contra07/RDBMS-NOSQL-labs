package ru.ssau.lab1new.service;

public interface CRUDService<E, I> 
{
    E Create(E entity) throws ServiceException;
    E Read(I id) throws ServiceException;
    Iterable<E> ReadAll() throws ServiceException;
    E Update(E entity) throws ServiceException;
    void Delete(I id) throws ServiceException;
}
