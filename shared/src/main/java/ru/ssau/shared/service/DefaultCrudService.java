// package ru.ssau.shared.service;

// import lombok.RequiredArgsConstructor;
// import ru.ssau.shared.pojo.*;
// import java.util.*;

// import org.springframework.data.repository.CrudRepository;

// @RequiredArgsConstructor
// public class DefaultCrudService<P extends IdPojo<I>, E, I>  implements CRUDService<P, I> 
// {
//     private final CrudRepository<E, I> repository;
//     private final PojoFactory<E, P> pojoFactory;

//     @Override
//     public P Create(P pojo) throws ServiceException 
//     {
//         Objects.requireNonNull(pojo);
//         try 
//         {
//             return pojoFactory.fromEntity(
//                 repository.save(pojoFactory.toEntity(pojo))
//             );
//         }
//         catch (PojoConversionException e)
//         {
//             throw new ServiceException("Ошибка конвертации элемента с идентификатором" + pojo.getId(), e);
//         }
//     }

//     @Override
//     public P Read(I id) throws ServiceException 
//     {
//         Objects.requireNonNull(id);
//         try 
//         {
//             var ids = new ArrayList<I>();
//             ids.add(id);
//             var item = Optional.of(repository.findAllById(ids).iterator().next());
//             return item.isPresent() ? pojoFactory.fromEntity(item.get()) : null;
//         } 
//         catch (PojoConversionException e)
//         {
//             throw new ServiceException("Ошибка конвертации элемента с идентификатором" + id, e);
//         }
//     }

//     @Override
//     public Iterable<P> ReadAll() throws ServiceException 
//     {
//         try 
//         {
//             var result = new ArrayList<P>();
//             var entities = repository.findAll();
//             for (var entity : entities) 
//             {
//                 try 
//                 {
//                     result.add(pojoFactory.fromEntity(entity));
//                 } 
//                 catch (PojoConversionException e)
//                 {
//                     throw new ServiceException("Ошибка при конвертации элемента " + entity.toString(), e);
//                 }
//             }
//             return result;
//         }
//         catch (Exception e) 
//         {
//             throw new ServiceException("Ошибка получения элементов", e);
//         }
//     }

//     @Override
//     public P Update(P pojo) throws ServiceException 
//     {
//         Objects.requireNonNull(pojo);
//         try
//         {
//             return pojoFactory.fromEntity(
//                 repository.save(pojoFactory.toEntity(pojo))
//             );
//         }
//         catch (PojoConversionException e)
//         {
//             throw new ServiceException("Ошибка конвертации элемента с идентификатором" + pojo.getId(), e);
//         }
//     }

//     @Override
//     public void Delete(I id) throws ServiceException 
//     {
//         try 
//         {
//             repository.deleteById(id);
//         } 
//         catch (Exception e) 
//         {
//             throw new ServiceException("Ошибка удаления элемента с идентификатором " + id, e);
//         }
//     }
// }
