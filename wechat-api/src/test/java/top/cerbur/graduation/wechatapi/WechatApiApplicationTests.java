package top.cerbur.graduation.wechatapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.cerbur.graduation.framework.bo.LostLocationBO;
import top.cerbur.graduation.framework.bo.LostSearchInputBO;
import top.cerbur.graduation.framework.bo.LostSearchOutputBO;
import top.cerbur.graduation.framework.dao.LostInfoDao;
import top.cerbur.graduation.framework.dao.LostLocationDao;
import top.cerbur.graduation.framework.dao.UserDao;
import top.cerbur.graduation.framework.entity.LostInfo;
import top.cerbur.graduation.framework.jwt.GenJWT;
import top.cerbur.graduation.framework.kafka.UniformMessageProducerService;
import top.cerbur.graduation.framework.vo.UserVO;
import top.cerbur.graduation.wechatapi.service.ILostService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class WechatApiApplicationTests {

    @Autowired
    UniformMessageProducerService uniformMessageProducerService;
    UserDao userDao;

    @Autowired
    LostInfoDao lostInfoDao;

    @Autowired
    LostLocationDao lostLocationDao;

    @Autowired
    ILostService lostService;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    @Test
    void contextLoads() throws UnsupportedEncodingException {
        System.out.println(GenJWT.encode(3));
    }


    @Test
    void testLostSearch() {
        List<String> stringList = new ArrayList<>();
//        stringList.add("3118007450");
        LostSearchInputBO build = LostSearchInputBO.builder().deleteStatus(0).build();
        List<LostSearchOutputBO> list = lostInfoDao.searchLost(build);

        list.forEach(System.out::println);
    }

    @Test
    void testLost() {
        LostInfo lostInfoById = lostInfoDao.getLostInfoById(13);
        System.out.println(lostInfoById);
    }

    @Test
    void testKafka() {
    }

    @Test
    void testLocation() {
        List<LostLocationBO> allLocation = lostLocationDao.getAllLocation();
        allLocation.forEach(System.out::println);
    }

    @Test
    void testNotice() {
        UserVO build = UserVO.builder().id(1).build();
        List<LostSearchOutputBO> list = lostService.searchNoticeLostInfoList(build);
        list.forEach(System.out::println);
    }

    @Test
    void testSearch() {
        LostSearchInputBO build = LostSearchInputBO.builder()
                .keyword("switch")
                .build();
        List<LostSearchOutputBO> list = lostService.searchLost(build);
        list.forEach(System.out::println);
    }
}
