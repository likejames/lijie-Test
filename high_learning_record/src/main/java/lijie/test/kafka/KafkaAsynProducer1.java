package lijie.test.kafka;

import com.alibaba.fastjson.JSONObject;
import lijie.test.threadPoolTest.ThreadPool;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.Callable;

/**
 * @author: Coline
 * @ClassName: KafkaAsynProducer
 * @Date: 2020/3/8 22:37
 * @Description: 异步模式发送kafka消息
 */
public class KafkaAsynProducer1 implements Callable {

    public static void main(String[] args) {
        ThreadPool.submit(new KafkaAsynProducer1());
    }

    public Object call() throws Exception {
        KafkaRequest kafkaRequest = new KafkaRequest();
        kafkaRequest.setOrgId(1);

        kafkaRequest.setSessionId("1");



        KafkaRequest kafkaRequest1 = new KafkaRequest();
        kafkaRequest1.setOrgId(1);

        kafkaRequest1.setSessionId("2222");
        KafkaProducer<String, String> producer = ProducerKafka.getProducer();
        try {
            for (int i = 60; i < 63; i++) {
                kafkaRequest1.setSessionId(String.valueOf(i));
                producer.send(new ProducerRecord("TestTopic1", "test", JSONObject.toJSONString(kafkaRequest1)), new Callback() {
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (null != exception) {
                            exception.printStackTrace();
                        }
                        if (null != metadata) {
                            System.out.println("offset: " + metadata.offset() + " "
                                    + "partition: " + metadata.partition());
                        }
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
        return null;
    }
}