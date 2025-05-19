package ru.ssau.lab3.repository.list;

import java.util.List;

import org.springframework.data.redis.connection.RedisListCommands.Direction;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListStringRepository 
{
    protected final StringRedisTemplate redisTemplate;
    protected String key;

    public String getKey()
    {
        return key;
    }
    
    public String get(int index)
    {
        System.out.println("Операция: LINDEX");
        return getOps().index(index);
    }

    public List<String> getRange(int start, int end)
    {
        System.out.println("Операция: LRANGE");
        return getOps().range(start, end);
    }

    public Long getSize()
    {
        System.out.println("Операция: LLEN");
        return getOps().size();
    }
    
    public Long putLeft(String...value)
    {
        System.out.println("Операция: LPUSH");
        return getOps().leftPushAll(value);
    }

    public Long putRight(String...value)
    {
        System.out.println("Операция: RPUSH");
        return getOps().rightPushAll(value);
    }

    public List<String> popLeft(int count)
    {
        System.out.println("Операция: LPOP");
        return getOps().leftPop(count);
    }

    public List<String> popRight(int count)
    {
        System.out.println("Операция: RPOP");
        return getOps().rightPop(count);
    }
    
    public Long find(String value)
    {
        System.out.println("Операция: LPOS");
        return getOps().indexOf(value);
    }

    public void trim(int start, int end)
    {
        System.out.println("Операция: LTRIM");
        getOps().trim(start, end);
    }

    public String move(String targetKey, boolean fromSourceFirst, boolean toTargetFirst)
    {
        System.out.println("Операция: LMOVE");
        var sourceDestination = fromSourceFirst ? Direction.LEFT : Direction.RIGHT;
        var targetDestination = toTargetFirst ? Direction.LEFT : Direction.RIGHT;
        return getOps().move(sourceDestination, targetKey, targetDestination);
    }

    private BoundListOperations<String, String> getOps()
    {
        return redisTemplate.boundListOps(key);
    }
}
