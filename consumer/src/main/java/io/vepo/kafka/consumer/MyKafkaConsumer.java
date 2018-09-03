package io.vepo.kafka.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class MyKafkaConsumer {
    private static Consumer<Long, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BROKER"));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // Create the consumer using props.
        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(System.getenv("KAFKA_TOPIC")));
        return consumer;

    }

    public static void main(String... args) {
        Consumer<Long, String> consumer = createConsumer();
        while (true) {
            final ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            if (consumerRecords.count() == 0) {
            }
            consumerRecords.forEach(record -> {
                System.out.printf("Consumer Record: (%d, %s, %d, %d)\n", record.key(), record.value(),
                        record.partition(), record.offset());
            });
            consumer.commitAsync();
        }
    }
}