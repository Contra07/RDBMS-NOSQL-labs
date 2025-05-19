package ru.ssau.lab3.repository.list;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FirstListStringRepository extends ListStringRepository
{
    public FirstListStringRepository(StringRedisTemplate redisTemplate) 
    {
        super(redisTemplate);
        this.key = "first_name_list";
    }
}
