package top.cerbur.graduation.wechatapi.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cerbur.graduation.framework.bo.UserBO;
import top.cerbur.graduation.framework.dao.UserDao;
import top.cerbur.graduation.framework.dao.cache.UserCacheDao;
import top.cerbur.graduation.framework.entity.User;
import top.cerbur.graduation.framework.jwt.GenJWT;
import top.cerbur.graduation.framework.vo.UserVO;
import top.cerbur.graduation.wechatapi.service.IUserService;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private WxMaService wxMaService;

    private UserDao userDao;

    private UserCacheDao userCacheDao;
    @Autowired
    public void setWxMaService(WxMaService wxMaService) {
        this.wxMaService = wxMaService;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String getOpenId(String code) throws WxErrorException {
        return wxMaService.getUserService().getSessionInfo(code).getOpenid();
    }

    @Autowired
    public void setUserCacheDao(UserCacheDao userCacheDao) {
        this.userCacheDao = userCacheDao;
    }

    @Override
    public Integer newUser(String openId) {
        User user = User.builder()
                .role(1)
                .openId(openId)
                .name("")
                // TODO 这里做个随机
                .nickName("新用户")
                .schoolId("")
                .className("")
                .build();

        userDao.createNewUser(user);
        return user.getId();
    }

    @Override
    public UserVO loginByOpenId(String openId) throws UnsupportedEncodingException {
        User user = userDao.getUserByOpenId(openId);
        Integer userId;
        if (user == null) {
            userId = newUser(openId);
        } else {
            userId = user.getId();
        }
        String token = GenJWT.encode(userId);
        return UserVO.builder()
                .id(userId)
                .token(token)
                .build();
    }

    @Override
    public UserVO getUserVOById(Integer userId) {
        User user = userCacheDao.getCache(userId);
        if (user == null) {
            user = userDao.getUserById(userId);
            if (user == null) {
                return null;
            }
            userCacheDao.putCache(user);
        }
        return UserVO.builder()
                .id(user.getId())
                .role(user.getRole())
                .className(user.getClassName())
                .name(user.getName())
                .nickName(user.getNickName())
                .schoolId(user.getSchoolId())
                .openId(user.getOpenId())
                .avatar(user.getAvatar())
                .build();

    }

    @Override
    public UserVO updateUser(UserBO userBO) {
        userCacheDao.deleteCache(userBO.getId());
        userDao.updateUserInfo(userBO);
        UserVO userVOById = getUserVOById(userBO.getId());
        userVOById.setOpenId("");
        return userVOById;
    }
}
