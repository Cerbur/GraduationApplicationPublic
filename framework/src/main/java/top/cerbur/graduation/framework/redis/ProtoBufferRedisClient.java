package top.cerbur.graduation.framework.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author cerbur
 */
@Component
public class ProtoBufferRedisClient {
    private RedisTemplate<String, ?> redisTemplate;

    private RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, ?> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setIntegerRedisTemplate(RedisTemplate<String, String> stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setBuffer(String key, byte[] buffer) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.set(key.getBytes(), buffer);
            return null;
        });
    }

    public void deleteKey(String key) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.del(key.getBytes());
            return null;
        });
    }

    public byte[] getBuffer(String key) {
        return redisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(key.getBytes()));
    }

    public void setBufferEx(String key, byte[] buffer, long seconds) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.setEx(key.getBytes(), seconds, buffer);
            return null;
        });
    }

    public void addSet(String key, String value) {
        stringRedisTemplate.opsForSet().add(key, value);
    }

    public void delSetValue(String key, String value) {
        stringRedisTemplate.opsForSet().remove(key, value);
    }

    public Set<String> getSet(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

}
