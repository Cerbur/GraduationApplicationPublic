package top.cerbur.graduation.framework.dao.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.cerbur.graduation.framework.bo.LostSearchOutputBO;
import top.cerbur.graduation.framework.proto.ProtoClient;
import top.cerbur.graduation.framework.redis.CacheConstant;
import top.cerbur.graduation.framework.redis.ProtoBufferRedisClient;

/**
 * @author cerbur
 */
@Component
@Slf4j
public class LostInfoCacheDao {

    private ProtoBufferRedisClient protoBufferRedisClient;

    @Autowired
    public void setProtoBufferRedisClient(ProtoBufferRedisClient protoBufferRedisClient) {
        this.protoBufferRedisClient = protoBufferRedisClient;
    }

    private String genCacheKey(Integer id) {
        return CacheConstant.LOST_INFO_CACHE_KEY + id;
    }

    public void putCache(LostSearchOutputBO lostSearchOutputBO) {
        ProtoClient<LostSearchOutputBO> client = new ProtoClient<>(LostSearchOutputBO.class);
        byte[] protoBuff = client.getProtoBuff(lostSearchOutputBO);

        String key = genCacheKey(lostSearchOutputBO.getId());
        protoBufferRedisClient.setBuffer(key, protoBuff);

        log.info("putLostInfoCache:{}", key);
    }

    public LostSearchOutputBO getCache(Integer id) {

        String key = genCacheKey(id);
        byte[] protostuff = protoBufferRedisClient.getBuffer(key);
        ProtoClient<LostSearchOutputBO> client = new ProtoClient<>(LostSearchOutputBO.class);
        LostSearchOutputBO lostSearchOutputBO = client.getModel(protostuff);
        if (lostSearchOutputBO.getId() == null) {
            return null;
        }

        log.info("getLostInfoCache:{}", key);
        return lostSearchOutputBO;
    }

    public void deleteCache(Integer id) {
        String key = genCacheKey(id);
        protoBufferRedisClient.deleteKey(key);
        log.info("deleteLostInfoCache:{}", key);
    }
}
