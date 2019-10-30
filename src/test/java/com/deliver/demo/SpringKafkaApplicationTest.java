package com.deliver.demo;

import com.deliver.demo.config.kafka.KafkaProduct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaApplicationTest {

    @Autowired
    private KafkaProduct kafkaProduct;

    /**
     * kafka 消息发送测试
     */
    @Test
    public void testKafka() {
        for (int i = 0; i < 3; i++) {
            kafkaProduct.sendOne();

            try {
                Thread.sleep(3_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
