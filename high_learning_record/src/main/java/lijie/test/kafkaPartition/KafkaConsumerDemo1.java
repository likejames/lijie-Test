package lijie.test.kafkaPartition;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * @author Dongguabai
 * @date 2019/1/17 11:55
 */
public class KafkaConsumerDemo1 extends Thread {

    private final KafkaConsumer<Integer, String> kafkaConsumer;

    public KafkaConsumerDemo1(String topic) {
        //构建相关属性
        //@see ConsumerConfig
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.211:9092");
        //消费组
        /**
         * consumer group是kafka提供的可扩展且具有容错性的消费者机制。既然是
         一个组，那么组内必然可以有多个消费者或消费者实例((consumer instance),
         它们共享一个公共的ID，即group ID。组内的所有消费者协调在一起来消费订
         阅主题(subscribed topics)的所有分区(partition)。当然，每个分区只能由同一
         个消费组内的一个consumer来消费.后面会进一步介绍。
         */
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaConsumerDemo");

        /** auto.offset.reset 参数  从什么时候开始消费
         *  public static final String AUTO_OFFSET_RESET_CONFIG = "auto.offset.reset";
         *
         *  这个参数是针对新的groupid中的消费者而言的，当有新groupid的消费者来消费指定的topic时，对于该参数的配置，会有不同的语义
         *  auto.offset.reset=latest情况下，新的消费者将会从其他消费者最后消费的offset处开始消费topic下的消息
         *  auto.offset.reset= earliest情况下，新的消费者会从该topic最早的消息开始消费
         auto.offset.reset=none情况下，新的消费组加入以后，由于之前不存在 offset，则会直接抛出异常。说白了，新的消费组不要设置这个值
         */

        //enable.auto.commit
        //消费者消费消息以后自动提交，只有当消息提交以后，该消息才不会被再次接收到（如果没有 commit，消息可以重复消费，也没有 offset），还可以配合auto.commit.interval.ms控制自动提交的频率。
        //当然，我们也可以通过consumer.commitSync()的方式实现手动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        /**max.poll.records
         *此参数设置限制每次调用poll返回的消息数，这样可以更容易的预测每次poll间隔
         要处理的最大值。通过调整此值，可以减少poll间隔
         */

        //间隔时间
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        //反序列化 key
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        //反序列化 value
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        //构建 KafkaConsumer
        kafkaConsumer = new KafkaConsumer(properties);
        //设置 topic
        kafkaConsumer.subscribe(Collections.singletonList(topic));

        //自己设置分区
//        TopicPartition partition = new TopicPartition(topic, 1);
//        kafkaConsumer.assign(Arrays.asList(partition));
    }


    /**
     * 接收消息
     */
    @Override
    public void run() {
        while (true) {
            //拉取消息
            ConsumerRecords<Integer, String> consumerRecord = kafkaConsumer.poll(100000000);
            for (ConsumerRecord<Integer, String> record : consumerRecord) {
                //record.partition() 获取当前分区
                System.out.println("kafka1: "+record.partition() + "】】  message receive 【" + record.value() + "】");
            }
        }
    }

    public static void main(String[] args) {
        new KafkaConsumerDemo1("test2").start();
    }

}