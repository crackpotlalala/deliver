package com.deliver.demo.config.kafka;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.Map;

@Configuration
public class KafkaConfig {

    @Autowired
    KafkaProperties properties;  //自动加载application中kafka的配置项（不包括自定义的配置项）
    @Value("${spring.kafka.consumer.group-id.one}")
    private String groupOne;
    @Value("${spring.kafka.consumer.group-id.two}")
    private String groupTwo;

    @Bean
    public ProducerFactory<String, String> kafkaProducerFactory() {
        Map<String, Object> producerProperties = properties.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(producerProperties);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }

    /**
     * 用于实现多个consumer在不同group
     */
    @Bean
    public ConsumerFactory<String, String> kafkaConsumerFactoryOne() {
        Map<String, Object> consumerProperties = properties.buildConsumerProperties();
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupOne);
        return new DefaultKafkaConsumerFactory<>(consumerProperties);
    }

    @Bean
    public ConsumerFactory<String, String> kafkaConsumerFactoryTwo() {
        Map<String, Object> consumerProperties = properties.buildConsumerProperties();
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupTwo);
        return new DefaultKafkaConsumerFactory<>(consumerProperties);
    }

    @Bean(name = "kafkaListenerContainerFactory")  //这里一定要命名为kafkaListenerContainerFactory，否则报错
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactoryOne() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactoryOne());
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactoryTwo() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactoryTwo());
        return factory;
    }

}
