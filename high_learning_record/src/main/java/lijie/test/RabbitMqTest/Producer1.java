package lijie.test.RabbitMqTest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import grizzled.slf4j.Logging;
import lijie.test.utils.ConnectionUtils;
import lijie.test.utils.ThreadPool;
import lombok.SneakyThrows;
import org.apache.flink.table.expressions.E;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer1 {
    private static final Logger logger = LoggerFactory.getLogger(Producer1.class);
    private static volatile int number = 0;

    public static void main(String[] args) throws IOException, TimeoutException {
        String exchangeName = "hello-exchange";
        //交换器根据消息携带的路由键，来决定消息交给哪个队列。交换机根据这个绑定规则来交给队列以后，消费者就可以连接
        String routingKey = "hola";
        for (int i = 0; i < 10; i++) {
            int j = i;
            Connection connection=ConnectionUtils.getConnection();
            Channel channel= ConnectionUtils.getChannel(connection);
            ThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    for (int  i = 0; i < 10000; i++) {
                        //发布消息
                        String message = j + " 你好啊: " + i;
                        byte[] messageBodyBytes = message.getBytes();
                        try {
                            channel.basicPublish(exchangeName, routingKey, null, messageBodyBytes);
                            System.out.println(j + " ****: " + i);
                            setNumber();
                        } catch (Exception e) {

                        }
                    }
                    try {
                        ConnectionUtils.close(channel,connection);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("生产发送的数量是： " + number);
                    }
                }
            });
        }
    }

    public static void setNumber() {
        number = number + 1;
    }
}