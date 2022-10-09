package top.cerbur.graduation.framework.dao.cache;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.cerbur.graduation.framework.entity.User;
import top.cerbur.graduation.framework.proto.ProtoClient;
import top.cerbur.graduation.framework.redis.CacheConstant;
import top.cerbur.graduation.framework.redis.ProtoBufferRedisClient;

/**
 * @author cerbur
 */
@Component
@Slf4j
public class UserCacheDao {
    private ProtoBufferRedisClient protoBufferRedisClient;

    @Autowired
    public void setProtoBufferRedisClient(ProtoBufferRedisClient protoBufferRedisClient) {
        this.protoBufferRedisClient = protoBufferRedisClient;
    }

    private String genCacheKey(Integer id) {
        return CacheConstant.USER_INFO_CACHE_KEY + id;
    }


    public void putCache(User user) {
        ProtoClient<User> client = new ProtoClient<>(User.class);
        byte[] protoBuff = client.getProtoBuff(user);

        String key = genCacheKey(user.getId());
        protoBufferRedisClient.setBuffer(key, protoBuff);

        log.info("putUserInfoCache:{}", key);
    }

    public User getCache(Integer id) {

        String key = genCacheKey(id);
        byte[] protostuff = protoBufferRedisClient.getBuffer(key);


        ProtoClient<User> client = new ProtoClient<>(User.class);
        User user = client.getModel(protostuff);
        if (user.getId() == null) {
            return null;
        }

        log.info("getUserInfoCache:{}", key);
        return user;
    }

    public void deleteCache(Integer id) {
        String key = genCacheKey(id);
        protoBufferRedisClient.deleteKey(key);
        log.info("deleteUserInfoCache:{}", key);
    }

}
