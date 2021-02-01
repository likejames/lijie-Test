package lijie.test.kafkaPartition;
 
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
 
import java.util.List;
import java.util.Map;
import java.util.Random;
 
/**
 * 消息发送后会调用自定义的策略
 *
 * @author Dongguabai
 * @date 2019/1/18 15:40
 */
public class MyPartitioner implements Partitioner {
 
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        //获取当前 topic 有多少个分区（分区列表）
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int partitionNum = 0;
        if (key == null) { //之前介绍过 Key 是可以传空值的
            partitionNum = new Random().nextInt(partitions.size());   //随机
        } else {
            //取 %
            partitionNum = Math.abs((key.hashCode()) % partitions.size());
        }
        System.out.println("key：" + key + "，value：" + value + "，partitionNum：" + partitionNum);
        //发送到指定分区
        return partitionNum;
    }
 
    @Override
    public void close() {
 
    }
 
    @Override
    public void configure(Map<String, ?> configs) {
 
    }
}