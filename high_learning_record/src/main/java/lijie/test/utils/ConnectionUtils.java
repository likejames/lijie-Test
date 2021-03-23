package lijie.test.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtils {

    public static Connection getConnection() throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("user");
        factory.setPassword("password");
        //设置 RabbitMQ 地址
        factory.setHost("114.116.105.151");
        //建立到代理服务器到连接
        Connection conn = factory.newConnection();
        return conn;
    }
    public static Channel getChannel( Connection connection) throws IOException, TimeoutException {
        //获得信道
        Channel channel = connection.createChannel();
        //声明交换器
        String exchangeName = "hello-exchange";
        channel.exchangeDeclare(exchangeName, "direct", true);
        String routingKey = "hola";//交换器根据消息携带的路由键，来决定消息交给哪个队列。交换机根据这个绑定规则来交给队列以后，消费者就可以连接
        String queueName = "Testqueue";
        //绑定队列，通过键 hola 将队列和交换器绑定起来
        channel.queueBind(queueName, exchangeName, routingKey);
        return channel;
    }

    public static void close(Channel channel,Connection connection) throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
