package ru.ssau.lab3.repository.crud;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.StringRedisTemplate;

import lombok.RequiredArgsConstructor;
import ru.ssau.lab3.model.IdEntity;
import ru.ssau.lab3.model.KeyValuePair;
import ru.ssau.shared.data.DataUtils;

@RequiredArgsConstructor
public class CrudRepository<T extends IdEntity<Integer>>
{
    protected final StringRedisTemplate redisTemplate;
    protected static final String ID_SEQUENCE = "_id_seq";
    protected final T entity;

    public Optional<T> findById(Integer id) 
    {
        return findById(id.toString());
    }

    public Optional<T> findById(String id) 
    {
        if(redisTemplate.hasKey(getKey(id, "id")))
        {
            Map<String, String> values = DataUtils.getFieldsWithValue(entity)
                .entrySet()
                .stream()
                .collect(
                    Collectors.toMap(
                        field -> field.getKey().getName(),
                        field -> {
                            return redisTemplate
                            .boundValueOps(getKey(id, field.getKey().getName()))
                            .get();
                        }
                    )
                );
            return Optional.of((T)entity.fromStringValues(values));
        }
        else
        {
            return Optional.empty();
        }
    }

    public List<T> findAll()
    {
        return redisTemplate
            .keys(getKey("*", "id"))
            .stream()
            .map(key -> findById(key.split(":")[1]))
            .filter(item -> item.isPresent())
            .map(item -> item.get())
            .sorted((item1, item2) -> item1.getId() - item2.getId())
            .toList();
    }

    public long deleteById(Integer id) 
    {
        var filedNames = redisTemplate.keys(getKey(id, "*"));
        return redisTemplate.unlink(filedNames);
    }

    public Optional<T> edit(Integer id, T entity)
    {
        if(redisTemplate.hasKey(getKey(id, "id")))
        {
            var values = DataUtils.getFieldsWithValue(entity)
                .entrySet()
                .stream()
                .collect(
                    Collectors.toMap(
                        field -> field.getKey().getName(),
                        field -> 
                        {
                            var ops = redisTemplate.boundValueOps(getKey(id, field.getKey().getName()));
                            ops.set(field.getValue().toString());
                            return ops.get();
                        }
                    )
                );

            return Optional.of((T)entity.fromStringValues(values));
        }
        else
        {
            return Optional.empty();
        }
    }

    public Optional<T> create(T entity) 
    {
        if(entity.getId() == null) 
        {
            var newId = redisTemplate.boundValueOps(entity.getClass().getSimpleName().toLowerCase() + ID_SEQUENCE).increment();
            if(newId != null)
            {
                entity.setId(newId.intValue());
            }
            else
            {
                return Optional.empty();
            }
        }

        var values = DataUtils.getFieldsWithValue(entity)
            .entrySet()
            .stream()
            .collect(
                Collectors.toMap(
                    field -> field.getKey().getName(),
                    field -> 
                    {
                        var ops = redisTemplate.boundValueOps(getKey(entity.getId(), field.getKey().getName()));
                        ops.set(field.getValue().toString());
                        return ops.get();
                    }
                )
            );
        return Optional.of((T)entity.fromStringValues(values));
    }

    public List<KeyValuePair> findKeys()
    {
        return redisTemplate.keys(getKey("*", "*"))
            .stream()
            .map(
                key -> new KeyValuePair(key, redisTemplate.boundValueOps(key).get())
            )
            .toList();
    }

    protected String getKey(Integer id, String field)
    {
        return getKey(id.toString(), field);
    }

    protected String getKey(String id, String field)
    {
        return String.format("%s:%s:%s", entity.getClass().getSimpleName().toLowerCase(), id, field);
    }

}