package ru.ssau.lab3.repository.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PubSubRepository 
{
    protected final StringRedisTemplate redisTemplate;
    protected Map<String, List<RedisConnection>> subscribeConnections = new HashMap<>();

    public Long publish(String channel, String message)
    {
        System.out.println("Операция PUBLISH");
        return redisTemplate.convertAndSend(channel, message);
    }

    public boolean subscribe(String channel, BiConsumer<String,String> onMassage)
    {
        if(channel == null || onMassage == null)
            return false;


        var factory = redisTemplate.getConnectionFactory();
        if (factory == null) 
            return false;

        var connection = factory.getConnection();
        new Thread(
            () -> 
            {
                System.out.println("Операция SUBSCRIBE");
                connection.subscribe(
                    (message, pattern) -> 
                    {
                        onMassage.accept(new String(message.getChannel()),new String(message.getBody()));
                    }, 
                    channel.getBytes()
                );
            }
        ).start();
        
        subscribeConnections.putIfAbsent(channel, new ArrayList<>());
        return subscribeConnections.get(channel).add(connection);
    }

    public long unsubscribe(String channel)
    {
        if(channel == null)
            return -1;

        var channelConnections = subscribeConnections.get(channel);
        if(channelConnections != null)
        {
            var result = 0;
            for (RedisConnection connection : channelConnections) 
            {
                var sub = connection.getSubscription();
                if(sub != null)
                {
                    System.out.println("Операция UNSUBSCRIBE");
                    sub.unsubscribe();
                    result++;
                }
            }
            return result;
        }
        else
        {
            return -1;
        }
    }
}
