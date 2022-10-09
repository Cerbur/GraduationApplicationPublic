package top.cerbur.graduation.framework.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class KafkaConfig {
    private String topicUniformMessage;


    @Value("${kafka.topic.uniformMessage}")
    public void setTopicUniformMessage(String topicUniformMessage) {
        this.topicUniformMessage = topicUniformMessage;
    }

    public String getTopicUniformMessage() {
        return topicUniformMessage;
    }

    /**
     * JSON消息转换器
     */
    @Bean
    public RecordMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }

    @Bean
    public NewTopic myTopic() {
        return new NewTopic(topicUniformMessage, 1, (short) 1);
    }

}
