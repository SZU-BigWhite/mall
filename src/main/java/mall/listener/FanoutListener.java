package mall.listener;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FanoutListener {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,//临时队列
                    exchange = @Exchange(name = "logs",type = "fanout") //绑定Exchange
            )
    })
    public void getMsg1(String msg){
        System.out.println("Fanout1消费开始:"+msg);
    }


    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,//临时队列
                    exchange = @Exchange(name = "logs",type = "fanout")

            )
    })
    public void getMsg2(String msg){
        System.out.println("Fanout2:"+msg);
    }
}
