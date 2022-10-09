package top.cerbur.graduation.wechatmq.kafka;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import top.cerbur.graduation.framework.dto.UniformMessageDTO;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UniformMessageConsumerService {

    private String topicUniformMessage;

    private WxMaService wxMaService;

    @Value("${kafka.topic.uniformMessage}")
    public void setTopicUniformMessage(String topicUniformMessage) {
        this.topicUniformMessage = topicUniformMessage;
    }

    @Autowired
    public void setWxMaService(WxMaService wxMaService) {
        this.wxMaService = wxMaService;
    }

    @KafkaListener(topics = {"${kafka.topic.uniformMessage}"}, groupId = "group1")
    public void consumeMessage(UniformMessageDTO dto) {
        log.info("消费者消费{}的消息 -> {}", topicUniformMessage, dto.toString());
        List<WxMaSubscribeMessage.MsgData> dataList = new ArrayList<>();
        dataList.add(new WxMaSubscribeMessage.MsgData("thing4", dto.getLocation()));
        dataList.add(new WxMaSubscribeMessage.MsgData("thing5", dto.getDescription()));
        dataList.add(new WxMaSubscribeMessage.MsgData("thing6", dto.getLostName()));
        dataList.add(new WxMaSubscribeMessage.MsgData("character_string7", dto.getSchoolId()));
        WxMaSubscribeMessage build = WxMaSubscribeMessage
                .builder()
                .toUser(dto.getOpenId())
                .templateId("hhzDaLTtvyYiLqLrU7-lScjOvaZjDYmq6IWvTVyCOeU")
                .page("/pages/lost-detail/lost-detail?id=" + dto.getLostId())
                .miniprogramState(WxMaConstants.MiniProgramState.TRIAL)
                .data(dataList)
                .build();
        try {
            wxMaService.getMsgService().sendSubscribeMsg(build);
        } catch (WxErrorException e) {
            log.error(e.getMessage());
        }
    }
}
