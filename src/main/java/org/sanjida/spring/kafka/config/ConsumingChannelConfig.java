package org.sanjida.spring.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.sanjida.spring.kafka.messaging.CountDownLatchHandler;
import org.sanjida.spring.kafka.messaging.domain.EventPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
@PropertySource("classpath:application.properties")
public class ConsumingChannelConfig {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.customer.topic}")
    private String customerKafkaTopic;

    @Bean
    public DirectChannel consumingChannel(){
        return new DirectChannel();
    }

    @Bean
    public KafkaMessageDrivenChannelAdapter<String, EventPayload> kafkaMessageDrivenChannelAdapter(){
        KafkaMessageDrivenChannelAdapter<String, EventPayload> kafkaMessageDrivenChannelAdapter =
                new KafkaMessageDrivenChannelAdapter<String, EventPayload>(kafkaListenerContainer());
        kafkaMessageDrivenChannelAdapter.setOutputChannel(consumingChannel());

        return kafkaMessageDrivenChannelAdapter;
    }


    @Bean(name = "countDownLatchHandler")
    @ServiceActivator(inputChannel = "consumingChannel")
    public CountDownLatchHandler countDownLatchHandler(){
        return new CountDownLatchHandler();
    }

    @SuppressWarnings("unchecked")
    @Bean
    public ConcurrentMessageListenerContainer<String, EventPayload> kafkaListenerContainer(){
        ContainerProperties containerProperties = new ContainerProperties(customerKafkaTopic);

        return (ConcurrentMessageListenerContainer<String, EventPayload>)
                new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProperties);
    }

    @Bean
    public ConsumerFactory<?,?> consumerFactory(){
        return new DefaultKafkaConsumerFactory<String, EventPayload>(consumerConfig(), new StringDeserializer(), new JsonDeserializer<>(EventPayload.class));
    }

    @Bean
    public Map<String, Object> consumerConfig(){
        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "identity-messaging");
        consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return consumerConfig;
    }

}
