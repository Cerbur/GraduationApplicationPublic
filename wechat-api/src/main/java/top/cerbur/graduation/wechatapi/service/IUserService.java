package top.cerbur.graduation.wechatapi.service;

import me.chanjar.weixin.common.error.WxErrorException;
import top.cerbur.graduation.framework.bo.UserBO;
import top.cerbur.graduation.framework.vo.UserVO;

import java.io.UnsupportedEncodingException;

/**
 * @author cerbur
 */
public interface IUserService {
    String getOpenId(String code) throws WxErrorException;

    Integer newUser(String openId);

    UserVO loginByOpenId(String openId) throws UnsupportedEncodingException;

    UserVO getUserVOById(Integer userId);

    UserVO updateUser(UserBO userBO);
}
