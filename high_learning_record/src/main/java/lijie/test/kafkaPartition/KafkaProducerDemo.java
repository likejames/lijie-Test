package lijie.test.kafkaPartition;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Dongguabai
 * @date 2019/1/17 11:26
 */
public class KafkaProducerDemo extends Thread {
    /**
     * 消息发送者
     */
    private final KafkaProducer<Integer, String> producer;

    /**
     * topic
     */
    private final String topic;

    private final Boolean isAsync;

    public KafkaProducerDemo(String topic, Boolean isAsync) {
        this.isAsync = isAsync;
        //构建相关属性
        //@see ProducerConfig
        Properties properties = new Properties();
        //Kafka 地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.211:9092");
        //kafka 客户端 Demo
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducerDemo");
        //The number of acknowledgments the producer requires the leader to have received before considering a request complete. This controls the durability of records that are sent.
        /**发送端消息确认模式：
         *  0：消息发送给broker后，不需要确认（性能较高，但是会出现数据丢失，而且风险最大，因为当 server 宕机时，数据将会丢失）
         *  1：只需要获得集群中的 leader节点的确认即可返回
         *  -1/all:需要 ISR 中的所有的 Replica进行确认（集群中的所有节点确认），最安全的，也有可能出现数据丢失（因为 ISR 可能会缩小到仅包含一个 Replica）
         */
        properties.put(ProducerConfig.ACKS_CONFIG, "-1");

        /**【调优】
         * batch.size 参数（默认 16kb）
         *  public static final String BATCH_SIZE_CONFIG = "batch.size";
         *
         *  producer对于同一个 分区 来说，会按照 batch.size 的大小进行统一收集进行批量发送，相当于消息并不会立即发送，而是会收集整理大小至 16kb.若将该值设为0，则不会进行批处理
         */

        /**【调优】
         * linger.ms 参数
         *  public static final String LINGER_MS_CONFIG = "linger.ms";
         *  一个毫秒值。Kafka 默认会把两次请求的时间间隔之内的消息进行搜集。相当于会有一个 delay 操作。比如定义的是1000（1s），消息一秒钟发送5条，那么这 5条消息不会立马发送，而是会有一个 delay操作进行聚合，
         *  delay以后再次批量发送到 broker。默认是 0，就是不延迟（同 TCP Nagle算法），那么 batch.size 也就不生效了
         */
        //linger.ms 参数和batch.size 参数只要满足其中一个都会发送

        /**【调优】
         * max.request.size 参数（默认是1M）   设置请求最大字节数
         * public static final String MAX_REQUEST_SIZE_CONFIG = "max.request.size";
         * 如果设置的过大，发送的性能会受到影响，同时写入接收的性能也会受到影响。
         */

        //设置 key的序列化，key 是 Integer类型，使用 IntegerSerializer
        //org.apache.kafka.common.serialization
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer");
        //设置 value 的序列化
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        //指定分区策略
//        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "lijie.test.kafkaPartition.MyPartitioner");
        //构建 kafka Producer，这里 key 是 Integer 类型，Value 是 String 类型
        producer = new KafkaProducer<Integer, String>(properties);
        this.topic = topic;
    }

    public static void main(String[] args) {
        new KafkaProducerDemo("test3", true).start();
    }

    @Override
    public void run() {
        int num = 0;
        while (num < 100) {
            UserMessage userMessage = new UserMessage();
            userMessage.setId(num);
            if (isAsync) {  //如果是异步发送
                producer.send(new ProducerRecord<Integer, String>(topic, JSONObject.toJSONString(userMessage)), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (metadata != null) {
                            System.out.println("async-offset：" + metadata.offset() + "-> partition" + metadata.partition());
                        }
                    }
                });
            } else {   //同步发送
                try {
                    RecordMetadata metadata = producer.send(new ProducerRecord<Integer, String>(topic, JSONObject.toJSONString(userMessage))).get();
                    System.out.println("sync-offset：" + metadata.offset() + "-> partition" + metadata.partition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            num++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}