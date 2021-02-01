package lijie.test.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

public class ProducerKafka {
    private static volatile KafkaProducer<String, String> producer = null;

    private ProducerKafka() {

    }

    private static KafkaProducer<String, String> init() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.1.211:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer(properties);
        return producer;
    }

    public static KafkaProducer<String, String> getProducer() {
        if (producer == null) {
            synchronized (KafkaAsynProducer.class) {
                if (producer == null) {
                    producer = init();
                }
            }
        }
        return producer;
    }
}
