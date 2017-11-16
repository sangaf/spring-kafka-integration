package org.sanjida.spring.kafka.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;


@Slf4j
@Component
public class CountDownLatchHandler implements MessageHandler {

    private CountDownLatch latch = new CountDownLatch(10);

    public CountDownLatch getLatch(){
        return latch;
    }


    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        log.info("message=received-form-kafka-[{}]", message);
        latch.countDown();
    }
}
