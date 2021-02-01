package lijie.test.kafka;

import lijie.test.threadPoolTest.ThreadPool;

public class Test {
    public static void main(String[] args) {
//        ThreadPool.submit(new KafkaAsynProducer());
        ThreadPool.submit(new KafkaConsumer());
    }
}
