package ru.ssau.lab3.repository.set;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SetStringRepository 
{
    protected final StringRedisTemplate redisTemplate;
    protected String key;

    public String getKey()
    {
        return key;
    }

    public Set<String> getDifference(String secondKey)
    {
        System.out.println("Операция SDIFF");
        return getOps().difference(secondKey);
    }

    public Set<String> getUnion(String secondKey)
    {
        System.out.println("Операция SUNION");
        return getOps().union(secondKey);
    }

    public Set<String> getIntersect(String secondKey)
    {
        System.out.println("Операция SINTER");
        return getOps().intersect(secondKey);
    }

    public Long getSize()
    {
        System.out.println("Операция SCARD");
        return getOps().size();
    }

    public Set<String> getAll()
    {
        System.out.println("Операция SMEMBERS");
        return getOps().members();
    }

    public Collection<String> getRandom(int count, boolean distinct)
    {
        System.out.println("Операция SRANDMEMBER");
        if(distinct)
            return getOps().distinctRandomMembers(count);
        else
            return getOps().randomMembers(count);
    }

    public Long add(String...values)
    {
        System.out.println("Операция SADD");
        return getOps().add(values);   
    }

    public Long remove(Object...values)
    {
        System.out.println("Операция SREM");
        return getOps().remove(values);
    }

    public Map<Object, Boolean> isMember(Object...values)
    {
        System.out.println("Операция SMISMEMBER");
        return getOps().isMember(values);
    }

    private BoundSetOperations<String, String> getOps()
    {
        return redisTemplate.boundSetOps(key);
    }
}
