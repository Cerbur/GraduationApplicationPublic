package top.cerbur.graduation.wechatmq;

import cn.binarywang.wx.miniapp.api.WxMaService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WechatMqApplicationTests {

    WxMaService wxMaService;

    @Autowired
    public void setWxMaService(WxMaService wxMaService) {
        this.wxMaService = wxMaService;
    }


    @Test
    void contextLoads() throws WxErrorException {
        String accessToken = wxMaService.getAccessToken();
        System.out.println(accessToken);
    }

}
