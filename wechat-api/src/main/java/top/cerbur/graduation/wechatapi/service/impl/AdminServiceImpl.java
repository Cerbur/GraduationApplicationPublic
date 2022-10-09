package top.cerbur.graduation.wechatapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cerbur.graduation.framework.bo.LostReviewBO;
import top.cerbur.graduation.framework.bo.ScanCodeBO;
import top.cerbur.graduation.framework.bo.UserBO;
import top.cerbur.graduation.framework.dao.LostInfoDao;
import top.cerbur.graduation.framework.dao.LostReviewDao;
import top.cerbur.graduation.framework.dao.cache.LostInfoCacheDao;
import top.cerbur.graduation.framework.dao.storage.ScanStorageCodeDao;
import top.cerbur.graduation.framework.jwt.GenJWT;
import top.cerbur.graduation.wechatapi.exception.CodeTokenException;
import top.cerbur.graduation.wechatapi.exception.CodeTokenIsUseException;
import top.cerbur.graduation.wechatapi.exception.CodeTokenNoSureException;
import top.cerbur.graduation.wechatapi.exception.CodeTokenNoUseException;
import top.cerbur.graduation.wechatapi.service.IAdminService;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

/**
 * @author cerbur
 */
@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {

    private ScanStorageCodeDao scanStorageCodeDao;


    private LostInfoDao lostInfoDao;

    private LostReviewDao lostReviewDao;

    private LostInfoCacheDao lostInfoCacheDao;


    @Autowired
    public void setScanStorageCodeDao(ScanStorageCodeDao scanStorageCodeDao) {
        this.scanStorageCodeDao = scanStorageCodeDao;
    }

    @Autowired
    public void setLostInfoDao(LostInfoDao lostInfoDao) {
        this.lostInfoDao = lostInfoDao;
    }

    @Autowired
    public void setLostReviewDao(LostReviewDao lostReviewDao) {
        this.lostReviewDao = lostReviewDao;
    }

    @Autowired
    public void setLostInfoCacheDao(LostInfoCacheDao lostInfoCacheDao) {
        this.lostInfoCacheDao = lostInfoCacheDao;
    }

    @Override
    public String genQRCodeToken(String uaCode) {
        ScanCodeBO build = ScanCodeBO.builder().id(0).uaCode(uaCode).isScan(false).build();
        String uuid = UUID.randomUUID().toString();
        scanStorageCodeDao.putStorage(uuid, build);
        return uuid;
    }

    @Override
    public String checkQRCodeToken(String uuid, String uaCode) throws UnsupportedEncodingException {

        ScanCodeBO storage = scanStorageCodeDao.getStorage(uuid);
        if (storage == null) {
            throw new CodeTokenException();
        }
        // 校验设备凭证
        if (!storage.getUaCode().equals(uaCode)) {
            throw new CodeTokenException();
        }
        if (storage.getId() == 0) {
            throw new CodeTokenNoUseException();
        }
        if (!storage.getIsScan()) {
            throw new CodeTokenNoSureException();
        }
        return GenJWT.encode(storage.getId());
    }

    @Override
    public void useQRCodeToken(String uuid, UserBO userBO) {
        ScanCodeBO storage = scanStorageCodeDao.getStorage(uuid);
        if (storage == null) {
            throw new CodeTokenException();
        }
        if (storage.getIsScan() || storage.getId() != 0) {
            throw new CodeTokenIsUseException();
        }
        ScanCodeBO build = ScanCodeBO.builder().id(userBO.getId()).isScan(false).uaCode(storage.getUaCode()).build();
        scanStorageCodeDao.putStorage(uuid, build);
    }

    @Override
    public void sureQRCodeToken(String uuid, UserBO userBO) {
        ScanCodeBO storage = scanStorageCodeDao.getStorage(uuid);
        if (storage == null) {
            throw new CodeTokenException();
        }
        //   这个码已经被确认了  or id 不相等
        if (storage.getIsScan() || !storage.getId().equals(userBO.getId())) {
            throw new CodeTokenIsUseException();
        }
        ScanCodeBO build = ScanCodeBO.builder().id(userBO.getId()).isScan(true).uaCode(storage.getUaCode()).build();
        scanStorageCodeDao.putStorage(uuid, build);
    }

    @Override
    public List<LostReviewBO> getLostReview(LostReviewBO bo) {
        return lostReviewDao.searchReview(bo);
    }

    @Override
    public void deleteLostInfo(Integer lostId, UserBO userBO) {
        lostInfoCacheDao.deleteCache(lostId);
        lostInfoDao.deleteLostInfoById(lostId);
    }

    @Override
    public void deleteLostReview(Integer reviewId, UserBO userBO) {
        lostReviewDao.deleteLostReviewById(reviewId);
    }
}
