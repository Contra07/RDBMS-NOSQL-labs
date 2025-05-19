package ru.ssau.lab3.repository.list;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SecondListStringRepository extends ListStringRepository
{
    public SecondListStringRepository(StringRedisTemplate redisTemplate) 
    {
        super(redisTemplate);
        this.key = "second_name_list";
    }
}
