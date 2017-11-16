package config;

import messaging.domain.EventPayload;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.MessageHandler;

import java.util.HashMap;
import java.util.Map;


@Configuration
@PropertySource("classpath:application.properties")
public class ProducingChannelConfig {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Bean
    public DirectChannel producingChannel(){
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "producingChannel")
    public MessageHandler kafkaMessageHandler(){
        KafkaProducerMessageHandler<String, EventPayload> handler = new KafkaProducerMessageHandler<>(payLoadTemplate());
        handler.setMessageKeyExpression(new LiteralExpression("identity-kafka"));

        return handler;
    }

    @Bean
    public Map<String, Object> producerConfigs(){
        Map<String, Object> kafkaConfig = new HashMap<>();
        kafkaConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        kafkaConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "IdentityChannel");
        kafkaConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        kafkaConfig.put(ProducerConfig.LINGER_MS_CONFIG, 1);

        return kafkaConfig;
    }

    @Bean
    public ProducerFactory<String, EventPayload> payloadProducerFactory(){
        return new DefaultKafkaProducerFactory<String, EventPayload>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, EventPayload> payLoadTemplate(){
        return new KafkaTemplate<String, EventPayload>(payloadProducerFactory());
    }

}
