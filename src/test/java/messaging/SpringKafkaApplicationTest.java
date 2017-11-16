package messaging;

import messaging.domain.EventPayload;
import messaging.domain.MessagePayload;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RunWith(SpringRunner.class)
@SpringBootTest(classes =app.SpringKafkaIntegrationApplication.class)
@ContextConfiguration
public class SpringKafkaApplicationTest {

    private static final Logger logger = LoggerFactory.getLogger(SpringKafkaApplicationTest.class);

    private static final String CUSTOMER_KAFKA_TOPIC = "customer.t";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CountDownLatchHandler countDownLatchHandler;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, CUSTOMER_KAFKA_TOPIC);

    @Test
    public void testIntegration() throws Exception{


        MessageChannel producingChannel = applicationContext.getBean("producingChannel", MessageChannel.class);

        Map<String, Object> headers = Collections.singletonMap(KafkaHeaders.TOPIC, CUSTOMER_KAFKA_TOPIC);


        for(int i=0; i<1;i++){
            MessagePayload payload = MessagePayload.builder().email("some.email." + i + "@example.com").id("id"+i).build();
            EventPayload eventPayload = EventPayload.builder().eventName("create-customer").time(LocalDateTime.now()).messagePayload(payload).build();

            Message<EventPayload> message = new GenericMessage<EventPayload>(eventPayload, headers);
            producingChannel.send(message);

        }

        countDownLatchHandler.getLatch().await(10000, TimeUnit.MILLISECONDS);
        System.out.println(countDownLatchHandler.getLatch().getCount());
    }


}
