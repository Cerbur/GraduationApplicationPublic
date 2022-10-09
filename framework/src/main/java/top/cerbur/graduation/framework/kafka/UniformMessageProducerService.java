package top.cerbur.graduation.framework.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import top.cerbur.graduation.framework.dto.UniformMessageDTO;

@Slf4j
@Service
public class UniformMessageProducerService {
    private KafkaConfig kafkaConfig;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UniformMessageProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void setKafkaConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    public void sendMessage(UniformMessageDTO o) {
        log.info("send message:{}", o.toString());
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(kafkaConfig.getTopicUniformMessage(), o);
        future.addCallback(result -> log.info("生产者成功发送消息到topic:{} partition:{}的消息", result.getRecordMetadata().topic(), result.getRecordMetadata().partition()),
                ex -> log.error("生产者发送消失败，原因：{}", ex.getMessage()));
    }
}
