package org.example.eventProducer;

import lombok.AllArgsConstructor;
import org.example.models.UserInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class UserInfoProducer {

    private final KafkaTemplate<String, UserInfoDto> kafkaTemplate;

   // @Value("${spring.kafka.topic.name}")
    private static final String TOPIC_NAME = "testingSelf";

    public void sendEventToKafka(UserInfoEvent userInfoEvent){
        Message<UserInfoEvent> message =
                MessageBuilder.withPayload(userInfoEvent)
                        .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                        .build();

        kafkaTemplate.send(message);

    }
}
