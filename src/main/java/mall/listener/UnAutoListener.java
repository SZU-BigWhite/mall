package mall.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class UnAutoListener {

    @RabbitListener(queuesToDeclare = @Queue("UnAutoQueue"))
    public void getMsg(String msg, Channel channel, Message message){
        System.out.println("获得消息："+msg);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
