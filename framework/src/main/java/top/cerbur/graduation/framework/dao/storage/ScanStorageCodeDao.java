package top.cerbur.graduation.framework.dao.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.cerbur.graduation.framework.bo.ScanCodeBO;
import top.cerbur.graduation.framework.proto.ProtoClient;
import top.cerbur.graduation.framework.redis.ProtoBufferRedisClient;
import top.cerbur.graduation.framework.redis.StorageConstant;

/**
 * @author cerbur
 */
@Component
@Slf4j
public class ScanStorageCodeDao {
    private ProtoBufferRedisClient protoBufferRedisClient;

    private static final long TIMEOUT = 60;

    @Autowired
    public void setProtoBufferRedisClient(ProtoBufferRedisClient protoBufferRedisClient) {
        this.protoBufferRedisClient = protoBufferRedisClient;
    }


    private String genStorageKey(String uuid) {
        return StorageConstant.SCAN_CODE_STORAGE_KEY + uuid;
    }

    public void putStorage(String uuid, ScanCodeBO scanCodeBO) {
        ProtoClient<ScanCodeBO> client = new ProtoClient<>(ScanCodeBO.class);
        byte[] protoBuff = client.getProtoBuff(scanCodeBO);

        String key = genStorageKey(uuid);
        protoBufferRedisClient.setBufferEx(key, protoBuff, TIMEOUT);

        log.info("putScanCodeStorage:{}", key);
    }

    public ScanCodeBO getStorage(String uuid) {

        String key = genStorageKey(uuid);
        byte[] protostuff = protoBufferRedisClient.getBuffer(key);
        ProtoClient<ScanCodeBO> client = new ProtoClient<>(ScanCodeBO.class);
        ScanCodeBO scanCodeBO = client.getModel(protostuff);
        log.info("getScanCodeStorage:{}", key);
        if (scanCodeBO.getId() == null) {
            return null;
        }
        return scanCodeBO;
    }

}
