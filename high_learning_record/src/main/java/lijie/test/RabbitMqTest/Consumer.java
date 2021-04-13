package lijie.test.RabbitMqTest;
import com.rabbitmq.client.*;
import lijie.test.utils.ThreadPool;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
public class Consumer implements Runnable {
    public static  int  messageNumber =0;

    public static void main(String[] args) throws IOException, TimeoutException {
        ThreadPool.submit(new Consumer());
    }
    public static void setNumber(){
        messageNumber=messageNumber+1;
    }

    @SneakyThrows
    @Override
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("123123");
        //设置 RabbitMQ 地址
        factory.setHost("192.168.1.72");
        //建立到代理服务器到连接
        Connection conn = factory.newConnection();
        //获得信道
        final Channel channel = conn.createChannel();
        //声明交换器
        String exchangeName = "hello-exchange";
        channel.exchangeDeclare(exchangeName, "direct", true);
        //声明队列
//        String queueName = channel.queueDeclare().getQueue();
        String queueName = "Testqueue";
        String routingKey = "hola";
        //绑定队列，通过键 hola 将队列和交换器绑定起来
        channel.queueBind(queueName, exchangeName, routingKey);
            //消费消息
            boolean autoAck = false;
            String consumerTag = "";
            channel.basicConsume(queueName, autoAck, consumerTag, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
//                    System.out.println("Consumer消费的路由键：" + routingKey);
//                    System.out.println("Consumer消费的内容类型：" + contentType);
                    long deliveryTag = envelope.getDeliveryTag();
                    //确认消息
                    channel.basicAck(deliveryTag, false);
//                    System.out.println("Consumer消费的消息体内容：");
                    String bodyStr = new String(body, "UTF-8");
                    System.out.println("Consumer消费的数据： "+bodyStr);
                    setNumber();
                    System.out.println("Consumer消费数量: "+messageNumber);
                }
            });
        }
}