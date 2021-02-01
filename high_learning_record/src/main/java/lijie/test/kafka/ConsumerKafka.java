package lijie.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

/**
 * kafuka客户端
 */
public class ConsumerKafka {
    private static volatile org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer = null;

    private ConsumerKafka() {

    }

    private static org.apache.kafka.clients.consumer.KafkaConsumer<String, String> init(String groudId) {
        Properties properties = new Properties();
        // kafka群组集群地址
        properties.put("bootstrap.servers", "192.168.1.211:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // 消费群组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groudId);
        org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer(properties);
        return consumer;
    }

    public static org.apache.kafka.clients.consumer.KafkaConsumer<String, String> getProducer(String groudId) {
        if (consumer == null) {
            synchronized (ConsumerKafka.class) {
                if (consumer == null) {
                    consumer = init(groudId);
                }
            }
        }
        return consumer;
    }
}
