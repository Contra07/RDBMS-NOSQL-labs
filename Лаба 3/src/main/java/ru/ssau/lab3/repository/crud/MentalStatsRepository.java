package ru.ssau.lab3.repository.crud;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import ru.ssau.lab3.model.MentalStats;

@Repository
public class MentalStatsRepository extends CrudRepository<MentalStats>
{
    public MentalStatsRepository(StringRedisTemplate redisTemplate, MentalStats entity) {
        super(redisTemplate, entity);
    }

    public long deleteByUser(Integer userId)
    {
        var idKeys = redisTemplate
            .keys(getKey("*", "userId"))
            .stream()
            .filter(
                userIdKey -> 
                {
                    var value = redisTemplate
                        .boundValueOps(userIdKey)
                        .get();
                    return value != null && value.equals(userId.toString());
                }
            )
            .flatMap(
                userIdKey -> redisTemplate
                    .keys(getKey(userIdKey.split(":")[1], "*"))
                    .stream()
            )
            .toList();
        return redisTemplate.unlink(idKeys);
    }

}
