package io.vepo.kafka.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class MyKafkaProducer {

    private static Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BROKER"));
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    public static void main(String... args) {
        final Producer<Long, String> producer = createProducer();
        try {
            String topic = System.getenv("KAFKA_TOPIC");
            int index = 0;
            while (true) {
                final ProducerRecord<Long, String> record = new ProducerRecord<>(topic, "Hello World " + index + "!");
                RecordMetadata metadata = producer.send(record).get();
                System.out.printf("sent record(key=%s value=%s) " + "meta(partition=%d, offset=%d)\n", record.key(),
                        record.value(), metadata.partition(), metadata.offset());
                index++;
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            producer.flush();
            producer.close();
        }
    }
}