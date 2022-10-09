package top.cerbur.graduation.wechatapi.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.cerbur.graduation.framework.bo.UserBO;
import top.cerbur.graduation.framework.jwt.GenJWT;
import top.cerbur.graduation.framework.result.Result;
import top.cerbur.graduation.framework.result.Return;
import top.cerbur.graduation.framework.vo.UserVO;
import top.cerbur.graduation.wechatapi.service.IUserService;
import top.cerbur.graduation.wechatapi.validator.SchoolId;

import javax.validation.constraints.NotBlank;
import java.io.UnsupportedEncodingException;


@Slf4j
@Validated
@RestController
@CrossOrigin
public class UserController {

    private IUserService userService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<UserVO> login(
            @NotBlank(message = "code 为空")
            @RequestParam(value = "code") String code
    ) throws WxErrorException, UnsupportedEncodingException {
        String openId = userService.getOpenId(code);
        UserVO userVO = userService.loginByOpenId(openId);
        return Return.success(userVO);
    }

    @GetMapping("/jwt")
    public Result<String> getJWT() throws UnsupportedEncodingException {
        String encode = GenJWT.encode(1);
        return Return.success(encode);
    }

    @GetMapping("/user/info")
    @PreAuthorize("@UserAuthorize.access(#userVO)")
    public Result<UserVO> getUserInfo(@ModelAttribute UserVO userVO) {
        userVO.setOpenId("");
//        throw new NeedAuthorizeException();
        return Return.success(userVO);
    }

    @PutMapping("/user/info")
    @PreAuthorize("@UserAuthorize.access(#userVO)")
    public Result<UserVO> updateUserInfo(
            @RequestParam(value = "name", defaultValue = "")
            String name,
            @RequestParam(value = "nick_name", defaultValue = "匿名用户")
            String nickName,
            @RequestParam(value = "school_id", defaultValue = "")
            @SchoolId(message = "你填写了学号但学号格式不正确")
            String schoolId,
            @RequestParam(value = "class_name", defaultValue = "")
            String className,
            @RequestParam(value = "avatar", defaultValue = "")
            String avatar,

            @ModelAttribute UserVO userVO
    ) {
        UserBO build = UserBO.builder()
                .id(userVO.getId())
                .name(name)
                .nickName(nickName)
                .schoolId(schoolId)
                .className(className)
                .avatar(avatar)
                .build();
        UserVO bo = userService.updateUser(build);
        return Return.success(bo);
    }
}
