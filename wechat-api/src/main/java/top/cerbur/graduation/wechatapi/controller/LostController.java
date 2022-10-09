package top.cerbur.graduation.wechatapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.cerbur.graduation.framework.bo.*;
import top.cerbur.graduation.framework.result.Result;
import top.cerbur.graduation.framework.result.Return;
import top.cerbur.graduation.framework.vo.UserVO;
import top.cerbur.graduation.wechatapi.service.ILostService;

import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/lost")
@CrossOrigin
public class LostController {

    private ILostService lostService;

    @Autowired
    public void setLostService(ILostService lostService) {
        this.lostService = lostService;
    }

    @PostMapping("/new")
    @CrossOrigin
    @PreAuthorize("@UserAuthorize.access(#userVO)")
    public Result<Integer> postNewLostInfo(

            @RequestParam(value = "title",defaultValue = "一条失物招领")
                    String title,
            // 失物类型
            @RequestParam(value = "lost_type", defaultValue = "1")
                    Integer lostType,
            // 学号默认可以不填
            @RequestParam(value = "school_id",defaultValue = "")
                    String schoolId,
            // 失主也是学生证使用的
            @RequestParam(value = "lost_name",defaultValue = "")
                    String lostName,
            @RequestParam(value = "description",defaultValue = "")
                    String description,
            @RequestParam(value = "image",defaultValue = "")
                    String image,
            @RequestParam(value = "location",defaultValue = "")
                    String location,
            @ModelAttribute UserVO userVO

    ) {

        NewLostBO newLostBO = NewLostBO.builder()

                // 必要的3个内容
                .title(title)
                .postUser(userVO.getId())
                .lostType(lostType)

                .description(description)
                .image(image)
                .location(location)

                // 学生证需要的内容
                .schoolId(schoolId)
                .lostName(lostName)

                .build();

        Integer integer = lostService.postNewLostBO(newLostBO);
        return Return.success(integer);
    }


    @GetMapping("/about/me")
    @CrossOrigin
    @PreAuthorize("@UserAuthorize.access(#userVO)")
    public Result<List<LostSearchOutputBO>> getLostInfoAboutMe(
            @ModelAttribute UserVO userVO
    ) {
        LostSearchInputBO build = LostSearchInputBO.builder()
                .schoolId(userVO.getSchoolId())
                .className(userVO.getClassName())
                .build();
        return Return.success(lostService.searchSchoolCardAboutMeLost(build));
    }

    @CrossOrigin
    @GetMapping("/list")
    public Result<List<LostSearchOutputBO>> getLostList(
            @RequestParam(value = "keyword", required = false)
            String keyword,
            @RequestParam(value = "found_status", defaultValue = "0")
            Integer foundStatus
    ) {
        LostSearchInputBO build = LostSearchInputBO.builder()
                .keyword(keyword)
                .foundStatus(foundStatus)
                .build();
        return Return.success(lostService.searchLost(build));
    }

    @CrossOrigin
    @GetMapping("/info/{id}")
    public Result<LostSearchOutputBO> getLostInfoById(
            @PathVariable(value = "id") Integer id
    ) {
        LostSearchOutputBO res = lostService.getLostInfoById(id);
        return Return.success(res);
    }

    @DeleteMapping("/info")
    @CrossOrigin
    @PreAuthorize("@UserAuthorize.access(#userVO)")
    public Result<Integer> deleteLostInfoById(
            @RequestParam(value = "lost_id") Integer id,
            @ModelAttribute UserVO userVO
    ) {
        LostSearchInputBO build = LostSearchInputBO.builder()
                .id(id)
                .userId(userVO.getId())
                .build();
        Integer integer = lostService.deleteLostInfo(build);
        return Return.success(integer);
    }

    @PutMapping("/info/found_status")
    @CrossOrigin
    @PreAuthorize("@UserAuthorize.access(#userVO)")
    public Result<Integer> updateInfoFoundStatus(
            @RequestParam(value = "lost_id") Integer id,
            @ModelAttribute UserVO userVO
    ) {
        LostSearchInputBO build = LostSearchInputBO.builder()
                .id(id)
                .userId(userVO.getId())
                .build();
        Integer integer = lostService.updateLostInfoFoundStatus(build);
        return Return.success(integer);
    }

    @PostMapping("/review")
    @CrossOrigin
    @PreAuthorize("@UserAuthorize.access(#userVO)")
    public Result<LostReviewBO> postReview(
            @RequestParam(value = "info_id")
            Integer infoId,
            @RequestParam(value = "reply_user")
            Integer replyUser,
            @RequestParam(value = "content")
            String content,
            @ModelAttribute UserVO userVO
    ) {
        LostReviewBO build = LostReviewBO.builder()
                .postUser(userVO.getId())
                .replyUser(replyUser)
                .infoId(infoId)
                .content(content)
                .build();
        Integer lostId = lostService.postLostReview(build);
        LostReviewBO lostReviewById = lostService.getLostReviewById(lostId);
        return Return.success(lostReviewById);
    }

    @DeleteMapping("/review")
    @CrossOrigin
    @PreAuthorize("@UserAuthorize.access(#userVO)")
    public Result<Integer> deleteReview(
            @RequestParam(value = "review_id")
            Integer reviewId,
            @ModelAttribute UserVO userVO
    ) {
        LostReviewBO build = LostReviewBO.builder()
                .postUser(userVO.getId())
                .id(reviewId)
                .build();
        Integer integer = lostService.deleteLostReviewByIdAndUser(build);
        return Return.success(integer);
    }


    @GetMapping("/list/notice")
    @CrossOrigin
    @PreAuthorize("@UserAuthorize.access(#userVO)")
    public Result<List<LostSearchOutputBO>> getLostInfoListNotice(
            @ModelAttribute UserVO userVO
    ) {
        return Return.success(lostService.searchNoticeLostInfoList(userVO));
    }

    @DeleteMapping("/notice")
    @CrossOrigin
    @PreAuthorize("@UserAuthorize.access(#userVO)")
    public Result<String> removeNoticeById(
            @Min(1)
            @RequestParam(value = "info_id")
            Integer infoId,
            @ModelAttribute UserVO userVO
    ) {
        lostService.removeNoticeById(userVO, infoId);
        return Return.success();
    }


    @CrossOrigin
    @GetMapping("/review/{lost_id}")
    public Result<List<LostReviewBO>> getReview(
            @PathVariable(value = "lost_id") Integer id
    ) {
        List<LostReviewBO> lostReviewById = lostService.getLostReview(id);
        return Return.success(lostReviewById);
    }

    @GetMapping("/location")
    @CrossOrigin
    public Result<List<LostLocationBO>> getLostLocationList() {
        List<LostLocationBO> allLocationList = lostService.getAllLocationList();
        return Return.success(allLocationList);
    }

    @CrossOrigin
    @GetMapping("/type")
    public Result<List<LostTypeBO>> getLostTypeList() {
        List<LostTypeBO> allLocationList = lostService.getAllTypeList();
        return Return.success(allLocationList);
    }
}
