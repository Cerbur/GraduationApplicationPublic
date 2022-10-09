package top.cerbur.graduation.wechatapi.authorize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.cerbur.graduation.framework.vo.UserVO;
import top.cerbur.graduation.wechatapi.exception.NeedAuthorizeException;

@Slf4j
@Service("UserAuthorize")
public class UserAuthorize {
    public boolean access(UserVO userVO) {
        log.info(String.valueOf(userVO));
        if (userVO == null || userVO.getId() == 0) {
            throw new NeedAuthorizeException();
        }
        return true;
    }
}
