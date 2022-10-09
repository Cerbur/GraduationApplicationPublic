package top.cerbur.graduation.framework.dao.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.cerbur.graduation.framework.redis.ProtoBufferRedisClient;
import top.cerbur.graduation.framework.redis.StorageConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @author cerbur
 */
@Component
@Slf4j
public class ReviewStorageDao {
    private ProtoBufferRedisClient protoBufferRedisClient;

    @Autowired
    public void setProtoBufferRedisClient(ProtoBufferRedisClient protoBufferRedisClient) {
        this.protoBufferRedisClient = protoBufferRedisClient;
    }

    private String genKey(Integer userId) {
        return StorageConstant.USER_REVIEW_STORAGE_KEY + userId;
    }

    public void addReviewNotice(Integer userId, Integer lostId) {
        String key = genKey(userId);
        protoBufferRedisClient.addSet(key, lostId.toString());
    }

    public void removeReviewNotice(Integer userId, Integer lostId) {
        String key = genKey(userId);
        protoBufferRedisClient.delSetValue(key, lostId.toString());
    }

    public List<Integer> getReviewNoticeList(Integer userId) {
        String key = genKey(userId);
        Set<String> set = protoBufferRedisClient.getSet(key);
        List<Integer> res = new ArrayList<>();
        set.forEach(v -> {
            res.add(Integer.parseInt(v));
        });
        return res;
    }
}
