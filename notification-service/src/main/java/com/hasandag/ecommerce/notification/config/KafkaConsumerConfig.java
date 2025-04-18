package com.hasandag.ecommerce.notification.config;

import com.hasandag.ecommerce.notification.messaging.OrderNotificationEvent;
import com.hasandag.ecommerce.notification.messaging.UserNotificationEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:notification-service-group}")
    private String groupId;

    // Common consumer configuration
    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.hasandag.ecommerce.*");
        return props;
    }

    // Order notification consumer factory
    @Bean
    public ConsumerFactory<String, OrderNotificationEvent> orderNotificationConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(), 
                new StringDeserializer(),
                new JsonDeserializer<>(OrderNotificationEvent.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderNotificationEvent> orderNotificationKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderNotificationEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderNotificationConsumerFactory());
        return factory;
    }

    // User notification consumer factory
    @Bean
    public ConsumerFactory<String, UserNotificationEvent> userNotificationConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(), 
                new StringDeserializer(),
                new JsonDeserializer<>(UserNotificationEvent.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserNotificationEvent> userNotificationKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserNotificationEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userNotificationConsumerFactory());
        return factory;
    }
} 