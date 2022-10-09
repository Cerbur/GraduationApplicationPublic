package top.cerbur.graduation.wechatapi.service;

import com.qiniu.common.QiniuException;
import top.cerbur.graduation.framework.bo.*;
import top.cerbur.graduation.framework.vo.UserVO;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ILostService {
    Integer postNewLostBO(NewLostBO newLostBO);

    List<String> genSchoolIdListAboutMe(LostSearchInputBO lostSearchInputBO);

    List<LostSearchOutputBO> searchLost(LostSearchInputBO lostSearchInputBO);

    LostSearchOutputBO getLostInfoById(Integer id);

    Integer deleteLostInfo(LostSearchInputBO bo);

    Integer updateLostInfoFoundStatus(LostSearchInputBO bo);

    List<LostSearchOutputBO> searchNoticeLostInfoList(UserVO userVO);

    void removeNoticeById(UserVO userVO, Integer lostId);

    List<LostSearchOutputBO> searchSchoolCardAboutMeLost(LostSearchInputBO lostSearchInputBO);

    List<LostLocationBO> getAllLocationList();

    List<LostTypeBO> getAllTypeList();

    Integer postLostReview(LostReviewBO lostReviewBO);

    LostReviewBO getLostReviewById(Integer reviewId);

    Integer deleteLostReviewByIdAndUser(LostReviewBO lostReviewBO);

    List<LostReviewBO> getLostReview(Integer lostId);

    OcrOutputBO ocrImage(OcrInputBO ocrInputBO) throws QiniuException, ExecutionException, InterruptedException;
}
