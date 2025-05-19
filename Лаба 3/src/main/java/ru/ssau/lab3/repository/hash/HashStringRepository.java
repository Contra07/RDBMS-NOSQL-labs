package ru.ssau.lab3.repository.hash;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class HashStringRepository 
{
    protected final StringRedisTemplate redisTemplate;
    protected String key = "hash_key";

    public Object get(String key)
    {
        System.out.println("Операция: HGET");
        return getOps().get(key);
    }

    public List<Object> get(Collection<Object> keys)
    {
        System.out.println("Операция: HMGET");
        return getOps().multiGet(keys);
    }

    public Set<Object> getKeys()
    {
        System.out.println("Операция: HKEYS");
        return getOps().keys();
    }

    public List<Object> getValues()
    {
        System.out.println("Операция: HVALS");
        return getOps().values();
    }

    public Map<Object, Object> getEntries()
    {
        System.out.println("Операция: HGETALL");
        return getOps().entries();
    }

    public void put(Object key, Object value)
    {
        System.out.println("Операция: HSET");
        getOps().put(key, value);
    }

    public void putAll(Map<? extends Object, ? extends Object> entities)
    {
        System.out.println("Операция: HMSET");
        getOps().putAll(entities);
    }

    public Boolean putIfAbsent(Object key, Object value)
    {
        System.out.println("Операция: HSETNX");
        return getOps().putIfAbsent(key, value);
    }

    public Long delete(Object... keys)
    {
        System.out.println("Операция: HDEL");
        return getOps().delete(keys);
    }

    public Long increment(Object key, long delta)
    {
        System.out.println("Операция: HINCRBY");
        return getOps().increment(key, delta);
    }

    public Double increment(Object key, double delta)
    {
        System.out.println("Операция: HINCRBYFLOAT");
        return getOps().increment(key, delta);
    }

    public Map<Object, Object> random(int count)
    {
        System.out.println("Операция: HRANDFIELD");
        return getOps().randomEntries(count);
    }

    private BoundHashOperations<String, Object, Object> getOps()
    {
        return redisTemplate.boundHashOps(key);
    }
}
