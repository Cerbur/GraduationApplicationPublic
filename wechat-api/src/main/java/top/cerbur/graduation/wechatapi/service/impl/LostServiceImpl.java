package top.cerbur.graduation.wechatapi.service.impl;

import com.qiniu.common.QiniuException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cerbur.graduation.framework.bo.*;
import top.cerbur.graduation.framework.constant.LostConstant;
import top.cerbur.graduation.framework.dao.*;
import top.cerbur.graduation.framework.dao.cache.LostInfoCacheDao;
import top.cerbur.graduation.framework.dao.storage.ReviewStorageDao;
import top.cerbur.graduation.framework.dto.UniformMessageDTO;
import top.cerbur.graduation.framework.entity.LostInfo;
import top.cerbur.graduation.framework.entity.LostReview;
import top.cerbur.graduation.framework.entity.User;
import top.cerbur.graduation.framework.kafka.UniformMessageProducerService;
import top.cerbur.graduation.framework.ocr.OcrClient;
import top.cerbur.graduation.framework.qiniu.OssClient;
import top.cerbur.graduation.framework.vo.UserVO;
import top.cerbur.graduation.wechatapi.service.ILostService;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class LostServiceImpl implements ILostService {

    private UniformMessageProducerService uniformMessageProducerService;
    private LostInfoDao lostInfoDao;
    private UserDao userDao;
    private LostLocationDao lostLocationDao;
    private LostTypeDao lostTypeDao;

    private LostReviewDao lostReviewDao;

    private OssClient ossClient;

    private OcrClient ocrClient;
    private LostInfoCacheDao lostInfoCacheDao;

    private ReviewStorageDao reviewStorageDao;
    @Autowired
    public void setLostInfoDao(LostInfoDao lostInfoDao) {
        this.lostInfoDao = lostInfoDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setLostLocationDao(LostLocationDao lostLocationDao) {
        this.lostLocationDao = lostLocationDao;
    }

    @Autowired
    public void setUniformMessageProducerService(UniformMessageProducerService uniformMessageProducerService) {
        this.uniformMessageProducerService = uniformMessageProducerService;
    }

    @Autowired
    public void setLostTypeDao(LostTypeDao lostTypeDao) {
        this.lostTypeDao = lostTypeDao;
    }

    @Autowired
    public void setLostReviewDao(LostReviewDao lostReviewDao) {
        this.lostReviewDao = lostReviewDao;
    }

    @Autowired
    public void setOssClient(OssClient ossClient) {
        this.ossClient = ossClient;
    }

    @Autowired
    public void setOcrClient(OcrClient ocrClient) {
        this.ocrClient = ocrClient;
    }

    @Autowired
    public void setLostInfoCacheDao(LostInfoCacheDao lostInfoCacheDao) {
        this.lostInfoCacheDao = lostInfoCacheDao;
    }

    @Autowired
    public void setReviewStorageDao(ReviewStorageDao reviewStorageDao) {
        this.reviewStorageDao = reviewStorageDao;
    }

    @Override
    public Integer postNewLostBO(NewLostBO newLostBO) {

        LostInfo lostInfo = LostInfo.builder()
                .title(newLostBO.getTitle())
                .postUser(newLostBO.getPostUser())
                .foundStatus(LostConstant.FOUND_STATUS_N)
                .lostType(newLostBO.getLostType())
                .schoolId(newLostBO.getSchoolId())
                .lostName(newLostBO.getLostName())
                .description(newLostBO.getDescription())
                .image(newLostBO.getImage())
                .location(newLostBO.getLocation())
                .deleteStatus(LostConstant.DELETE_STATUS_N)
                .build();

        lostInfoDao.createNewLostInfo(lostInfo);

        Integer lostId = lostInfo.getId();
        if (LostConstant.TYPE_SCHOOL_CARD.equals(newLostBO.getLostType())) {
            User user = User.builder()
                    .schoolId(newLostBO.getSchoolId())
                    .name(newLostBO.getLostName())
                    .build();


            User lostUser = userDao.searchUserBySchoolIdAndName(user);

            if (lostUser != null) {
                UniformMessageDTO build = UniformMessageDTO.builder()
                        .lostId(lostId)
                        .openId(lostUser.getOpenId())
                        .schoolId(lostUser.getSchoolId())
                        .lostName(lostUser.getName())
                        .location(lostInfo.getLocation())
                        .description(lostInfo.getDescription())
                        .build();
                uniformMessageProducerService.sendMessage(build);
            }
        }
        return lostId;
    }

    @Override
    public List<String> genSchoolIdListAboutMe(LostSearchInputBO lostSearchInputBO) {
        List<String> emptyList = new ArrayList<>();
        emptyList.add("0");
        String schoolId = lostSearchInputBO.getSchoolId();
        if (schoolId.length() != LostConstant.SCHOOL_ID_LENGTH) {
            return emptyList;
        }
        Set<String> set = new HashSet<>();

        char[] sex = {'1', '2'};
        char[] schoolIdChars = schoolId.toCharArray();
        int range = 10;
        for (char c : sex) {
            // 获得男/女
            schoolIdChars[1] = c;
            long schoolInt = Long.parseLong(String.valueOf(schoolIdChars));
            for (int i = range * -1; i < range; i++) {
                set.add(schoolInt + i + "");
            }
        }
        List<String> schoolIdByClassName = userDao.getSchoolIdByClassName(lostSearchInputBO.getClassName());
        set.addAll(schoolIdByClassName);
        return new ArrayList<>(set);
    }


    @Override
    public List<LostSearchOutputBO> searchLost(LostSearchInputBO lostSearchInputBO) {
        // 只查询未删除的
        lostSearchInputBO.setDeleteStatus(LostConstant.DELETE_STATUS_N);
        return lostInfoDao.searchLost(lostSearchInputBO);
    }

    @Override
    public LostSearchOutputBO getLostInfoById(Integer id) {
        LostSearchOutputBO lostSearchOutputBO = lostInfoCacheDao.getCache(id);
        if (lostSearchOutputBO != null) {
            return lostSearchOutputBO;
        }
        LostSearchInputBO build = LostSearchInputBO.builder()
                .id(id)
                .build();
        List<LostSearchOutputBO> list = searchLost(build);
        LostSearchOutputBO res = null;
        if (list.size() != 0) {
            res = list.get(0);
            lostInfoCacheDao.putCache(res);
        }
        return res;
    }

    @Override
    public Integer deleteLostInfo(LostSearchInputBO bo) {
        lostInfoCacheDao.deleteCache(bo.getId());
        return lostInfoDao.deleteLostInfoByUserIdAndId(bo.getUserId(), bo.getId());
    }

    @Override
    public Integer updateLostInfoFoundStatus(LostSearchInputBO bo) {
        lostInfoCacheDao.deleteCache(bo.getId());
        return lostInfoDao.updateLostInfoFoundStatusByUserIdAndId(bo.getUserId(), bo.getId());
    }


    @Override
    public List<LostSearchOutputBO> searchNoticeLostInfoList(UserVO userVO) {
        List<Integer> reviewNoticeList = reviewStorageDao.getReviewNoticeList(userVO.getId());
        if (reviewNoticeList.size() == 0) {
            return new ArrayList<>();
        }
        LostSearchInputBO build = LostSearchInputBO.builder()
                .idList(reviewNoticeList)
                .deleteStatus(LostConstant.DELETE_STATUS_N)
                .build();

        return lostInfoDao.searchLost(build);
    }

    @Override
    public void removeNoticeById(UserVO userVO, Integer lostId) {
        reviewStorageDao.removeReviewNotice(userVO.getId(), lostId);
    }

    @Override
    public List<LostSearchOutputBO> searchSchoolCardAboutMeLost(LostSearchInputBO lostSearchInputBO) {
        List<String> schoolIdListAboutMe = genSchoolIdListAboutMe(lostSearchInputBO);

        LostSearchInputBO build = LostSearchInputBO.builder()
                // 我附近的学号，可能是我的同学 我们班的同学
                .schoolIdList(schoolIdListAboutMe)
                // 仅查询校园卡
                .lostType(LostConstant.TYPE_SCHOOL_CARD)
                // 搜索未删除的
                .deleteStatus(LostConstant.DELETE_STATUS_N)
                .build();


        return lostInfoDao.searchLost(build);
    }

    @Override
    public List<LostLocationBO> getAllLocationList() {
        return lostLocationDao.getAllLocation();
    }

    @Override
    public List<LostTypeBO> getAllTypeList() {
        return lostTypeDao.getAllType();
    }

    @Override
    public Integer postLostReview(LostReviewBO lostReviewBO) {
        LostReview lostReview = LostReview.builder()
                .infoId(lostReviewBO.getInfoId())
                .postUser(lostReviewBO.getPostUser())
                .replyUser(lostReviewBO.getReplyUser())
                .content(lostReviewBO.getContent())
                .deleteStatus(LostConstant.DELETE_STATUS_N)
                .build();
        lostReviewDao.insertLostReview(lostReview);
        if (!lostReviewBO.getPostUser().equals(lostReviewBO.getReplyUser())) {
            reviewStorageDao.addReviewNotice(lostReviewBO.getReplyUser(), lostReview.getInfoId());
        }
        return lostReview.getId();
    }

    @Override
    public LostReviewBO getLostReviewById(Integer reviewId) {
        LostReviewBO build = LostReviewBO.builder()
                .id(reviewId)
                .deleteStatus(LostConstant.DELETE_STATUS_N)
                .build();
        List<LostReviewBO> list = lostReviewDao.searchReview(build);
        LostReviewBO res = null;
        if (list.size() != 0) {
            res = list.get(0);
        }
        return res;
    }

    @Override
    public Integer deleteLostReviewByIdAndUser(LostReviewBO lostReviewBO) {
        return lostReviewDao.deleteLostReviewByIdAndUser(lostReviewBO.getPostUser(), lostReviewBO.getId());
    }

    @Override
    public List<LostReviewBO> getLostReview(Integer lostId) {
        LostReviewBO build = LostReviewBO.builder()
                .infoId(lostId)
                .deleteStatus(LostConstant.DELETE_STATUS_N)
                .build();
        return lostReviewDao.searchReview(build);
    }

    @Override
    public OcrOutputBO ocrImage(OcrInputBO ocrInputBO) throws QiniuException, ExecutionException, InterruptedException {
        CompletableFuture<HttpResponse<String>> future = null;
        if (ocrInputBO.getHasOcr()) {
            future = ocrClient.sendAsync(ocrInputBO.getBase64());
        }
        String url = ossClient.uploadImage(ocrInputBO.getBase64());
        OcrOutputBO.OcrOutputBOBuilder builder = OcrOutputBO.builder();
        builder.url(url);
        if (ocrInputBO.getHasOcr() && future != null) {
            HttpResponse<String> stringHttpResponse = future.get();
            List<String> result = ocrClient.getResult(stringHttpResponse);
            result.forEach((str) -> {
                String s = str.replaceAll(" ", "");

                if (s.contains("姓名:")) {
                    builder.name(s.substring(s.indexOf("姓名:") + 3));
                }
                if (s.contains("学号:")) {
                    builder.schoolId(s.substring(s.indexOf("学号:") + 3));
                }
            });
        }
        return builder.build();
    }
}
