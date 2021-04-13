package lijie.test.RabbitMqTest;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import lijie.test.dao.CallInfo;
import lijie.test.utils.ConnectionUtils;
import lijie.test.utils.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class CalloutInfoProducer {
    private static final Logger logger = LoggerFactory.getLogger(Producer1.class);
    private static volatile int number = 0;

    public static void main(String[] args) throws IOException, TimeoutException {
        String exchangeName = "hello-exchange";
        //交换器根据消息携带的路由键，来决定消息交给哪个队列。交换机根据这个绑定规则来交给队列以后，消费者就可以连接
        String routingKey = "hola";
        for (int i = 0; i < 1; i++) {
            int j = i;
            Connection connection= ConnectionUtils.getConnection();
            Channel channel= ConnectionUtils.getChannel(connection);
            ThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();    //获取开始时间
                    for (int  i = 0; i < 10000; i++) {
                        //发布消息
                        CallInfo callInfo=new CallInfo();
                        String message = JSONObject.toJSONString(callInfo);
                        byte[] messageBodyBytes = message.getBytes();
                        try {
                            //消息持久化
//                            channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, messageBodyBytes);
                            //消息不持久化
                            channel.basicPublish(exchangeName, routingKey, null, messageBodyBytes);
                            //单次提交
//                            if(channel.waitForConfirms())
//                            {
//                                System.out.println("发送成功");
//                            }
                            System.out.println(j + " ****: " + i);
                            setNumber();
                        } catch (Exception e) {

                        }
                    }
                    long endTime = System.currentTimeMillis();    //获取结束时间
                    System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
                    try {
                        //批量提交
//                        channel.waitForConfirms();
                        ConnectionUtils.close(channel,connection);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
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
