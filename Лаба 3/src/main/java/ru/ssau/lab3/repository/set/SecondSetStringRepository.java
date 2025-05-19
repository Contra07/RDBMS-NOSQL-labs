package ru.ssau.lab3.repository.set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SecondSetStringRepository extends SetStringRepository
{
    public SecondSetStringRepository(StringRedisTemplate redisTemplate) 
    {
        super(redisTemplate);
        key = "second_names_set";
    }

}
