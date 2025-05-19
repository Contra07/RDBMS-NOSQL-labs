package ru.ssau.lab3.repository.crud;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import ru.ssau.lab3.model.Demographics;

@Repository
public class DemographicsRepository extends CrudRepository<Demographics>
{
    public DemographicsRepository(StringRedisTemplate redisTemplate, Demographics entity) 
    {
        super(redisTemplate, entity);
    }
}
