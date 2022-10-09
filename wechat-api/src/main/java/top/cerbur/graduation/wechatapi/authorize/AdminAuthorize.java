package top.cerbur.graduation.wechatapi.authorize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.cerbur.graduation.framework.vo.UserVO;
import top.cerbur.graduation.wechatapi.exception.NeedAuthorizeException;

@Slf4j
@Service("AdminAuthorize")
public class AdminAuthorize {
    public boolean access(UserVO userVO) {
        log.info(String.valueOf(userVO));
        if (userVO == null || userVO.getId() == 0 || userVO.getRole() < 2) {
            throw new NeedAuthorizeException();
        }
        return true;
    }
}
