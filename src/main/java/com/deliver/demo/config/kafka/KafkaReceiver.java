package com.deliver.demo.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * kafka 消息消费者
 */
@Component
@Slf4j
public class KafkaReceiver {

    @KafkaListener(topics = {"test"}, containerFactory = "kafkaListenerContainerFactory")
    public void listen(String message) {
        System.out.println("=========== " + message + " =============");
    }

    @KafkaListener(topics = {"test2"}, containerFactory = "kafkaListenerContainerFactoryTwo")
    public void listenSecond(String message) {
        System.out.println("=========== " + message + " =============");

    }

}
