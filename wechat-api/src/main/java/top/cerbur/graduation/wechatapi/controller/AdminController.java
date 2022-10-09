package top.cerbur.graduation.wechatapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.cerbur.graduation.framework.bo.LostReviewBO;
import top.cerbur.graduation.framework.bo.LostSearchInputBO;
import top.cerbur.graduation.framework.bo.LostSearchOutputBO;
import top.cerbur.graduation.framework.bo.UserBO;
import top.cerbur.graduation.framework.result.Result;
import top.cerbur.graduation.framework.result.Return;
import top.cerbur.graduation.framework.vo.UserVO;
import top.cerbur.graduation.wechatapi.service.IAdminService;
import top.cerbur.graduation.wechatapi.service.ILostService;

import javax.validation.constraints.Min;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author cerbur
 */
@Slf4j
@Validated
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    private IAdminService adminService;

    private ILostService lostService;

    @Autowired
    public void setLostService(ILostService lostService) {
        this.lostService = lostService;
    }


    @Autowired
    public void setAdminService(IAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/code")
    @CrossOrigin
    public Result<String> genScanCodeKey(
            @RequestParam(value = "uuid")
            String uaCode
    ) {
        String s = adminService.genQRCodeToken(uaCode);
        return Return.success(s);
    }

    @GetMapping("/code/status")
    @CrossOrigin
    public Result<String> checkScanCodeKey(
            @RequestParam(value = "code")
            String code,
            @RequestParam(value = "uuid")
            String uaCode
    ) throws UnsupportedEncodingException {
        String s = adminService.checkQRCodeToken(code, uaCode);
        return Return.success(s);
    }

    @PreAuthorize("@AdminAuthorize.access(#userVO)")
    @PutMapping("/code/status")
    @CrossOrigin
    public Result<String> useScanCodeKey(
            @RequestParam(value = "code")
            String code,

            @ModelAttribute UserVO userVO

    ) {
        UserBO build = UserBO.builder().id(userVO.getId()).build();
        adminService.useQRCodeToken(code, build);
        return Return.success();
    }

    @PreAuthorize("@AdminAuthorize.access(#userVO)")
    @CrossOrigin
    @PutMapping("/code/sure")
    public Result<String> sureScanCodeKey(
            @RequestParam(value = "code")
            String code,

            @ModelAttribute UserVO userVO

    ) {
        UserBO build = UserBO.builder().id(userVO.getId()).build();
        adminService.sureQRCodeToken(code, build);
        return Return.success();
    }

    @PreAuthorize("@AdminAuthorize.access(#userVO)")
    @GetMapping("/lost/list")
    @CrossOrigin
    public Result<List<LostSearchOutputBO>> getAdminLostList(
            @ModelAttribute UserVO userVO
    ) {
        LostSearchInputBO build = LostSearchInputBO.builder()
                .build();
        return Return.success(lostService.searchLost(build));
    }

    @PreAuthorize("@AdminAuthorize.access(#userVO)")
    @GetMapping("/review/list")
    @CrossOrigin
    public Result<List<LostReviewBO>> getReviewList(
            @ModelAttribute UserVO userVO
    ) {
        LostReviewBO build = LostReviewBO.builder()
                .deleteStatus(0)
                .build();
        return Return.success(adminService.getLostReview(build));
    }

    @PreAuthorize("@AdminAuthorize.access(#userVO)")
    @CrossOrigin
    @DeleteMapping("/lost")
    public Result<String> deleteLostInfo(
            @RequestParam(value = "id")
            @Min(value = 1, message = "id 填写有误")
            Integer lostId,

            @ModelAttribute UserVO userVO

    ) {
        UserBO userBO = UserBO.builder()
                .id(userVO.getId())
                .name(userVO.getName())
                .build();
        adminService.deleteLostInfo(lostId, userBO);
        return Return.success();
    }

    @PreAuthorize("@AdminAuthorize.access(#userVO)")
    @CrossOrigin
    @DeleteMapping("/review")
    public Result<String> deleteLostReview(
            @RequestParam(value = "id")
            @Min(value = 1, message = "id 填写有误")
            Integer reviewId,

            @ModelAttribute UserVO userVO

    ) {
        UserBO userBO = UserBO.builder()
                .id(userVO.getId())
                .name(userVO.getName())
                .build();
        adminService.deleteLostReview(reviewId, userBO);
        return Return.success();
    }


}
