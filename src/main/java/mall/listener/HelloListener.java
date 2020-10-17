package mall.listener;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
//持久化 非独占  非自动删除
@RabbitListener(queuesToDeclare = @Queue(value = "helloQueue"))
public class HelloListener {

    @RabbitHandler
    public void getMsg(String msg){
        System.out.println("收到消息："+msg);
    }
}
