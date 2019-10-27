package com.deliver.demo.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * kafka 消息消费者
 */
@Component
@Slf4j
public class KafkaReceiver {

    @KafkaListener(topics = {"abc123"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();

            log.info("record =" + record);
            log.info("message =" + message);
        }
    }
}
