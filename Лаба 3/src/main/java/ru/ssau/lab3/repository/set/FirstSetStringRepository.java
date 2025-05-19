package ru.ssau.lab3.repository.set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FirstSetStringRepository extends SetStringRepository
{
    public FirstSetStringRepository(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
        this.key = "first_names_set";
    }
}
