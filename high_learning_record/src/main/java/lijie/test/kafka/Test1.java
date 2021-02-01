package lijie.test.kafka;

import lijie.test.threadPoolTest.ThreadPool;

public class Test1 {
    public static void main(String[] args) {
        ThreadPool.submit(new KafkaAsynProducer());
    }
}
