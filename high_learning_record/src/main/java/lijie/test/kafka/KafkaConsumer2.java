package lijie.test.kafka;

import lijie.test.threadPoolTest.ThreadPool;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.Collections;
import java.util.concurrent.Callable;

/**
 * @author: Coline
 * @ClassName: KafkaConsumer
 * @Date: 2020/3/7 22:16
 * @Description: 消费者代码，用于验证
 */
public class KafkaConsumer2 implements Callable {
    public static void main(String[] args) {
        ThreadPool.submit(new KafkaConsumer2());
    }

    public Object call() throws Exception {
        long time = 500;
        org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer = ConsumerKafka.getProducer("test");
        try {
            consumer.subscribe(Collections.singletonList("TestTopic"));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(time);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format("topic:%s,分区：%d,偏移量：%d," + "key:%s,value:%s", record.topic(), record.partition(),
                            record.offset(), record.key(), record.value()));
                }
            }
        } finally {
            consumer.close();
        }
    }
}