package top.cerbur.graduation.wechatapi.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import top.cerbur.graduation.framework.jwt.GenJWT;
import top.cerbur.graduation.framework.vo.UserVO;
import top.cerbur.graduation.wechatapi.service.IUserService;

import java.io.UnsupportedEncodingException;

@Slf4j
@ControllerAdvice
public class UserAdvice {

    private IUserService userService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public UserVO genUserVO(@RequestHeader(value = "Authorization", required = false) String jwt) {
        if (jwt == null) {
            return null;
        }
        log.info("jwt:"+jwt);
        Integer userId;
        try {
            userId = GenJWT.decode(jwt);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return userService.getUserVOById(userId);
    }

}
