package top.cerbur.graduation.wechatapi.service;

import top.cerbur.graduation.framework.bo.LostReviewBO;
import top.cerbur.graduation.framework.bo.UserBO;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IAdminService {
    String genQRCodeToken(String uaCode);

    String checkQRCodeToken(String uuid, String uaCode) throws UnsupportedEncodingException;

    void useQRCodeToken(String uuid, UserBO userBO);

    void sureQRCodeToken(String uuid, UserBO userBO);

    List<LostReviewBO> getLostReview(LostReviewBO lostReviewBO);

    void deleteLostInfo(Integer lostId, UserBO userBO);

    void deleteLostReview(Integer reviewId, UserBO userBO);

}
